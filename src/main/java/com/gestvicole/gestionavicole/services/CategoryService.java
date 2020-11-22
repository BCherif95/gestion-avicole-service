package com.gestvicole.gestionavicole.services;
import com.gestvicole.gestionavicole.entities.Building;
import com.gestvicole.gestionavicole.entities.Category;
import com.gestvicole.gestionavicole.repositories.CategoryRepository;
import com.gestvicole.gestionavicole.utils.ResponseBody;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public ResponseBody getAll() {
        try {
            List<Category> list = categoryRepository.findAll();
            list.sort(Collections.reverseOrder());
            return ResponseBody.with(list,"Liste de categories");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseBody.error("Une erreur est survenue");
        }
    }

    public ResponseBody getById(Long id) {
        try {
            Optional<Category> categoryOptional = categoryRepository.findById(id);
            return categoryOptional.map(category -> ResponseBody.with(category, "Recuperer avec succès"))
                    .orElseGet(() -> ResponseBody.error("Cette categorie n'existe pas"));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseBody.error("Une erreur est survenue");
        }
    }

    public ResponseBody save(Category category) {
        try {
            if (categoryRepository.existsByName(category.getName())) {
                return ResponseBody.error("Cette categorie existe déjà !");
            }
            return ResponseBody.with(categoryRepository.save(category),"Ajouter avec succès !");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseBody.error("Une erreur est survenue");
        }
    }

    public ResponseBody edit(Category category) {
        try {
            boolean isExist = categoryRepository.distinctByIdAndExistByName(category.getId(), category.getName()).isEmpty();
            if (!isExist) {
                return ResponseBody.error("Ce nom existe deja!");
            }
            categoryRepository.save(category);
            return ResponseBody.with(category, "Modifier avec succes!");

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseBody.error("Ce nom existe deja!");
        }
    }
}
