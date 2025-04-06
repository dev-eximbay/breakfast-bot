package org.example.breakfast.repository;

import org.example.breakfast.model.Menu;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Created by Robin on 25. 4. 5.
 * Description :
 */

public interface MenuRepository {
    Optional<String> fetchMenu(LocalDate date);

    void addMenu(Menu menu);

    void saveMenus(List<Menu> menus);

    void deleteMenu(LocalDate date);
}
