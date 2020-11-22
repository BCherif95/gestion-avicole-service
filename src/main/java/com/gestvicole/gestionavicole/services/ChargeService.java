package com.gestvicole.gestionavicole.services;

import com.gestvicole.gestionavicole.entities.Building;
import com.gestvicole.gestionavicole.entities.Charge;
import com.gestvicole.gestionavicole.entities.ChargeProduction;
import com.gestvicole.gestionavicole.entities.Production;
import com.gestvicole.gestionavicole.repositories.ChargeProductionRepository;
import com.gestvicole.gestionavicole.repositories.ChargeRepository;
import com.gestvicole.gestionavicole.repositories.ProductionRepository;
import com.gestvicole.gestionavicole.utils.ProjectUtils;
import com.gestvicole.gestionavicole.utils.ResponseBody;
import com.gestvicole.gestionavicole.utils.SearchBody;
import com.gestvicole.gestionavicole.utils.dashboard.ChargeGraphBody;
import com.gestvicole.gestionavicole.utils.dashboard.LineGraphBody;
import com.gestvicole.gestionavicole.wrapper.ChargeItem;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.*;
import java.util.function.Function;

import static java.util.Objects.nonNull;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

@Service
public class ChargeService {

    private final ChargeRepository chargeRepository;
    private final ProductionRepository productionRepository;
    private final ChargeProductionRepository chargeProductionRepository;

    public ChargeService(ChargeRepository chargeRepository,
                         ProductionRepository productionRepository,
                         ChargeProductionRepository chargeProductionRepository) {
        this.chargeRepository = chargeRepository;
        this.productionRepository = productionRepository;
        this.chargeProductionRepository = chargeProductionRepository;
    }

    public ResponseBody getCharge(Long id) {
        try {
            Optional<Charge> charge = chargeRepository.findById(id);
            return charge.map(charge1 -> ResponseBody.with(charge1, "Charge recuperer avec succès")).orElseGet(() -> ResponseBody.error("Cette charge n'existe pas"));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseBody.error("Une erreur est survenue");
        }
    }

    public ResponseBody findAll() {
        try {
            List<Charge> list = chargeRepository.findAll();
            list.sort(Collections.reverseOrder());
            return ResponseBody.with(list, "Les charges effectuées");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseBody.error("La liste est vide");
        }
    }

    public ResponseBody create(Charge charge) {
        try {
            Optional<Charge> optionalCharge = chargeRepository.findByDate(charge.getDate());
            if (optionalCharge.isPresent()) {
                return ResponseBody.error("Il exite déjà une charge à la date choisie");
            }
            List<Production> productions = productionRepository.findAllByDate(charge.getDate());
            if (productions.isEmpty()) {
                return ResponseBody.error("Aucune production à cette date");
            }
            Charge newCharge = chargeRepository.saveAndFlush(charge);
            // Creates production charges
            productions
                    .stream()
                    .map(production -> buildChargeProduction(production, newCharge))
                    .forEach(chargeProductionRepository::save);

            // Returns result
            return ResponseBody.with(newCharge, "Ajouter avec succes");

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseBody.error("Une erreur est survenue");
        }
    }

    private ChargeProduction buildChargeProduction(Production production, Charge charge) {
        return ChargeProduction
                .builder()
                .charge(charge)
                .production(production)
                .build();
    }

    public ResponseBody update(Charge charge) {
        try {
            Optional<Charge> chargeUpdating = chargeRepository.findById(charge.getId());
            if (!chargeUpdating.isPresent()) {
                return ResponseBody.error("Cette charge n'existe pas");
            }
            return ResponseBody.with(create(charge), "Modifier avec succès!");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseBody.error("Une erreur est survenue");
        }
    }

    public ResponseBody initChargeDash(SearchBody searchBody) {
        try {
            ChargeGraphBody chargeGraphBody = new ChargeGraphBody();

            //retrieve uncanceled invoice
            List<Global> globals = globals(searchBody.getDate());
            Function<Function<Global, Integer>, Integer> func = (mapper) -> globals
                    .stream()
                    .map(mapper)
                    .reduce(0, Integer::sum);

            chargeGraphBody.setSumTotalProd(func.apply(a -> a.total));
            chargeGraphBody.setSumTotalLoad(func.apply(a -> a.totalLoad));
            chargeGraphBody.setSumNetMargin(func.apply(a -> a.netMargin));
            chargeGraphBody.setSumTotalEffective(func.apply(a -> a.totalEffective));
            //graph
            initChargeGraphBody(chargeGraphBody);
            return ResponseBody.with(chargeGraphBody, "Evolution de la marge sur le sept dernier jour");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseBody.error("Une erreur est survenue");
        }
    }

    public ResponseBody getGroupInfos(Long chargeId) {
        List<ChargeProduction> cps = chargeProductionRepository.findByChargeId(chargeId);
        return ResponseBody.with(
                cps.stream()
                        .collect(groupingBy(ChargeProduction::getCharge))
                        .entrySet()
                        .stream()
                        .map(entry -> entry.getValue()
                                .stream()
                                .map(this::transformToChargeItem)
                                .collect(toList())
                        ).collect(toList()),
                "Succès"
        );
    }

    private List<Global> globals(Date date) {
        List<Charge> charges = nonNull(date)
                ? chargeRepository.findAllChargeByDate(date)
                : chargeRepository.findAll();
        List<Long> ids = charges.stream().map(Charge::getId).collect(toList());
        List<ChargeProduction> cps = ids.isEmpty() ? new ArrayList<>() : chargeProductionRepository.findAllByChargeIdIn(ids);

        return cps.stream()
                .collect(groupingBy(ChargeProduction::getCharge))
                .entrySet()
                .stream()
                .map(entry -> {
                            List<ChargeItem> chargeItems = entry
                                    .getValue()
                                    .stream()
                                    .map(this::transformToChargeItem)
                                    .collect(toList());
                            return new AbstractMap.SimpleEntry<>(entry.getKey(), chargeItems);
                        }
                )
                .map(items -> {
                    Integer costOfDay = items.getKey().getCostOfDay();
                    Integer dailyCharge = items.getKey().getDailyCharge();
                    List<ChargeItem> chargeItems = items.getValue();
                    Integer total = chargeItems.stream().map(ChargeItem::getTotal).reduce(0, Integer::sum);
                    Integer totalEffective = chargeItems.stream().map(ChargeItem::getEffective).reduce(0, Integer::sum);
                    Integer consumption = totalEffective * costOfDay;
                    Integer totalLoad = consumption + dailyCharge;
                    Integer netMargin = total - totalLoad;
                    return Global.create(total, consumption, totalEffective, totalLoad, netMargin);
                })
                .collect(toList());
    }

    private ChargeItem transformToChargeItem(ChargeProduction cp) {
        Charge charge = cp.getCharge();
        Production production = cp.getProduction();
        return ChargeItem.create(production.getBuilding().getName(),
                production.getOverallProduction(),
                production.getOverallProduction() * charge.getPrice(),
                production.getGeneralEffective());
    }

    private void initChargeGraphBody(ChargeGraphBody chargeGraphBody) {
        try {
            List<LineGraphBody> lineGraphBodies = new ArrayList<>();
            List<Date> last7days = ProjectUtils.getLast7Days();
            for (Date date : last7days) {
                String day = ProjectUtils.toString(date, "yyyy-MM-dd", Locale.FRENCH);
                List<Global> globals = globals(date);
                Function<Function<Global, Integer>, Integer> func = (mapper) -> globals
                        .stream()
                        .map(mapper)
                        .reduce(0, Integer::sum);
                Integer sumTotalLoad = func.apply(a -> a.totalLoad);
                Integer sumNetMargin = func.apply(a -> a.netMargin);
                lineGraphBodies.add(new LineGraphBody(day, sumTotalLoad == null ? 0 : sumTotalLoad,
                        sumNetMargin == null ? 0 : sumNetMargin));
            }
            chargeGraphBody.setLineGraphBodies(lineGraphBodies);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    static class Global {
        Integer total;
        Integer consumption;
        Integer totalEffective;
        Integer totalLoad;
        Integer netMargin;

        private Global(Integer total, Integer consumption, Integer totalEffective, Integer totalLoad, Integer netMargin) {
            this.total = total;
            this.consumption = consumption;
            this.totalEffective = totalEffective;
            this.totalLoad = totalLoad;
            this.netMargin = netMargin;
        }

        public static Global create(Integer total, Integer consumption, Integer totalEffective, Integer totalLoad, Integer netMargin) {
            return new Global(total, consumption, totalEffective, totalLoad, netMargin);
        }
    }
}
