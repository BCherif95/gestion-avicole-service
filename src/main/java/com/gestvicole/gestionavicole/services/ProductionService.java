package com.gestvicole.gestionavicole.services;

import com.gestvicole.gestionavicole.entities.Building;
import com.gestvicole.gestionavicole.entities.Production;
import com.gestvicole.gestionavicole.repositories.BuildingRepository;
import com.gestvicole.gestionavicole.repositories.ProductionRepository;
import com.gestvicole.gestionavicole.utils.ProjectUtils;
import com.gestvicole.gestionavicole.utils.ResponseBody;
import com.gestvicole.gestionavicole.utils.SearchBody;
import com.gestvicole.gestionavicole.utils.SearchDateBody;
import com.gestvicole.gestionavicole.utils.dashboard.LineGraphBody;
import com.gestvicole.gestionavicole.utils.dashboard.ProdDashBody;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ProductionService {

    private final ProductionRepository productionRepository;
    private final BuildingRepository buildingRepository;

    public ProductionService(ProductionRepository productionRepository,
                             BuildingRepository buildingRepository) {
        this.productionRepository = productionRepository;
        this.buildingRepository = buildingRepository;
    }

    public ResponseBody findAll() {
        try {
            return ResponseBody.with(productionRepository.findAll(), "Liste de productions");
        }catch (Exception e){
            e.printStackTrace();
            return ResponseBody.error("Une erreur est survenue!");
        }
    }

    public ResponseBody findByDateBetween(SearchDateBody searchDateBody) {
        try {
            return ResponseBody.with(productionRepository.findProdByDateBetween(searchDateBody.getStartDate(),
                    searchDateBody.getEndDate()),"Liste des productions !!");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseBody.error("Une erreur est survenue");
        }
    }

    public ResponseBody getProduction(Long id) {
        try {
            Optional<Production> production = productionRepository.findById(id);
            return production.map(production1 -> ResponseBody.with(production1, "Production recuperer avec succès")).orElseGet(() -> ResponseBody.error("Cette production n'existe pas"));
        }catch (Exception e) {
            e.printStackTrace();
            return ResponseBody.error("Une erreur est survenue");
        }
    }

    public ResponseBody getAllProdOfDay() {
        try {
            return ResponseBody.with(productionRepository.findAllByDate(new Date()),"Productions de la Journée");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseBody.error("Une erreur est survenue!");
        }
    }

    public ResponseBody findByBuildingIdAndDate(SearchBody searchBody) {
        try {
          return ResponseBody.with(productionRepository.findByBuildingIdAndDate(searchBody.getDate(),searchBody.getBuildingId()),"Productions");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseBody.error("Une erreur est survenue");
        }
    }

    public ResponseBody create(Production production) {
        try {
            List<Production> productions = productionRepository.findAllByDateAndBuildingId(production.getDate(),production.getBuilding().getId());
            if (!productions.isEmpty()){
                return ResponseBody.error("Il exite déjà une production a la date choisie");
            }
            Building building = buildingRepository.findById(production.getBuilding().getId()).get();
            production.setGeneralEffective(building.getTotalLayer());
            production.setCommercialProductions(production.getOverallProduction() - production.getAlveolusBroken());
            Production todayProduction = productionRepository.save(production);
            Integer generalEffectiveNew = building.getTotalLayer() - production.getMortality();
            building.setTotalLayer(generalEffectiveNew);
            buildingRepository.save(building);
            return ResponseBody.with(todayProduction,"Ajouter avec succès");
        }catch (Exception e) {
            e.printStackTrace();
            return ResponseBody.error("Une erreur est survenue!");
        }
    }

    public ResponseBody update(Production production) {
        try {
            Optional<Production> optionalProduction = productionRepository.findById(production.getId());
            if (!optionalProduction.isPresent()) {
                return ResponseBody.error("Une erreur est survenue!");
            }
            production.setCommercialProductions(production.getOverallProduction() - production.getAlveolusBroken());
            return ResponseBody.with(productionRepository.save(production),"Modifier avec succès");
        }catch (Exception e){
            e.printStackTrace();
            return ResponseBody.error("Une erreur est survenue!");
        }
    }

    public ResponseBody initProdDash(SearchBody searchBody) {
        try {
            int totalProd = 0;
            int totalEffective = 0;
            int totalMortality = 0;
            int totalAlveolusBroken = 0;

            ProdDashBody prodDashBody = new ProdDashBody();
            if (searchBody.getBuildingId()!=0 && searchBody.getDate() !=null) {
                Production production = productionRepository.findByBuildingIdAndDate(searchBody.getDate(),searchBody.getBuildingId());
               if (production != null){
                   prodDashBody.setTotalProd(production.getOverallProduction());
                   prodDashBody.setTotalEffective(production.getGeneralEffective());
                   prodDashBody.setTotalMortality(production.getMortality());
                   prodDashBody.setTotalAlveolusBroken(production.getAlveolusBroken());
               }else {
                   prodDashBody.setTotalProd(0);
                   prodDashBody.setTotalEffective(0);
                   prodDashBody.setTotalMortality(0);
                   prodDashBody.setTotalAlveolusBroken(0);
               }
            }else {
                List<Production> productionList = productionRepository.findAllByDate(new Date());
                totalProd = productionList
                        .stream()
                        .map(Production::getOverallProduction)
                        .reduce(0,Integer::sum);
                totalEffective = productionList
                        .stream()
                        .map(Production::getGeneralEffective)
                        .reduce(0, Integer::sum);
                totalMortality = productionList
                        .stream()
                        .map(Production::getMortality)
                        .reduce(0,Integer::sum);
                totalAlveolusBroken = productionList
                        .stream()
                        .map(Production::getAlveolusBroken)
                        .reduce(0,Integer::sum);
                prodDashBody.setTotalProd(totalProd);
                prodDashBody.setTotalEffective(totalEffective);
                prodDashBody.setTotalMortality(totalMortality);
                prodDashBody.setTotalAlveolusBroken(totalAlveolusBroken);
            }
            // graph body
            initProdGraphBody(prodDashBody, searchBody);
            return ResponseBody.with(prodDashBody, "Statistique Production Journalière");

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseBody.error("Une erreur est survenue!");
        }

    }

    private void initProdGraphBody(ProdDashBody prodDashBody, SearchBody searchBody) {
        try {
            List<LineGraphBody> lineGraphBodies = new ArrayList<>();
            List<Date> last7days = ProjectUtils.getLast7Days();
            for (Date date : last7days) {
                String day = ProjectUtils.toString(date, "yyyy-MM-dd", Locale.FRENCH);
                Integer sumTotalProd = productionRepository.sumTotalOverallProductionByDateAndBuildingId(date,searchBody.getBuildingId());
                Integer sumTotalEffective = productionRepository.sumTotalEffectiveByDateAndBuildingId(date,searchBody.getBuildingId());
                lineGraphBodies.add(new LineGraphBody(day,sumTotalProd == null ? 0 : sumTotalProd,
                        sumTotalEffective == null ? 0 : sumTotalEffective));
            }
            prodDashBody.setLineGraphBodies(lineGraphBodies);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
