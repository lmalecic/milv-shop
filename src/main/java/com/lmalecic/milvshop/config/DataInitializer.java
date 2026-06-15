package com.lmalecic.milvshop.config;

import com.lmalecic.milvshop.model.*;
import com.lmalecic.milvshop.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;
    private final NationRepository nationRepository;
    private final TankRoleRepository tankRoleRepository;
    private final TankRepository tankRepository;

    @Override
    public void run(String... args) throws Exception {
        this.initUserRoles();
        this.initAdminUser();
        this.initNations();
        this.initTankRoles();
        this.initTanks();
    }

    private void initUserRoles() {
        List<String> toCheck = List.of("ROLE_ADMIN", "ROLE_USER");
        Set<String> existing = new HashSet<>(userRoleRepository.findAllNames());
        userRoleRepository.saveAll(toCheck.stream()
                .filter(roleName -> !existing.contains(roleName))
                .map(roleName -> UserRole.builder().name(roleName).build())
                .toList());
    }

    private void initAdminUser() {
        if (!userRepository.existsByUsername("admin")) {
            userRepository.save(User.builder()
                    .username("admin")
                    .pwdHash(passwordEncoder.encode("password"))
                    .roles(List.of(userRoleRepository.findByName("ROLE_ADMIN")
                                            .orElse(UserRole.builder()
                                                            .name("ROLE_ADMIN")
                                                            .build())))
                    .build());
        }
    }

    private void initNations() {
        record NationSeed(String name, String imgPath) {}
        List<NationSeed> toCheck = List.of(
                new NationSeed("USA", "/img/nation/usa.svg"),
                new NationSeed("German Reich", "/img/nation/german_reich.svg"),
                new NationSeed("USSR", "/img/nation/ussr.svg"),
                new NationSeed("Great Britain", "/img/nation/uk.svg")
        );
        Set<String> existing = new HashSet<>(nationRepository.findAllNames());
        nationRepository.saveAll(toCheck.stream()
                .filter(seed -> !existing.contains(seed.name()))
                .map(seed -> Nation.builder()
                        .name(seed.name())
                        .imgPath(seed.imgPath())
                        .build())
                .toList()
        );
    }

    private void initTankRoles() {
        record TankRoleSeed(String name, String imgPath) {}
        List<TankRoleSeed> toCheck = List.of(
                new TankRoleSeed("Light tank", "/img/tankrole/light_tank.svg"),
                new TankRoleSeed("Medium tank", "/img/tankrole/medium_tank.svg"),
                new TankRoleSeed("Heavy tank", "/img/tankrole/heavy_tank.svg")
        );
        Set<String> existing = new HashSet<>(tankRoleRepository.findAllNames());
        tankRoleRepository.saveAll(toCheck.stream()
                .filter(seed -> !existing.contains(seed.name()))
                .map(seed -> TankRole.builder()
                        .name(seed.name())
                        .imgPath(seed.imgPath())
                        .build())
                .toList()
        );
    }

    private void initTanks() {
        record TankSeed(Nation nation, TankRole role, String name, String imgPath, String description, int price, int mainGunCalibre, int armorThickness, int maxSpeed, int crewSize) {}

        Map<String, Nation> nations = nationRepository.findAll().stream()
                .collect(Collectors.toMap(Nation::getName, nation -> nation));
        Map<String, TankRole> tankRoles = tankRoleRepository.findAll().stream()
                .collect(Collectors.toMap(TankRole::getName, role -> role));

        List<TankSeed> toCheck = List.of(
                new TankSeed(nations.get("German Reich"),
                        tankRoles.get("Heavy tank"),
                        "Tiger II",
                        "/img/tank/tiger2.png",
                        "The Panzerkampfwagen Tiger Ausf. B (Sd.Kfz. Index: Sd.Kfz. 182), also known as the Tiger II or informally the Königstiger (lit. \"King Tiger\"), was a German heavy tank developed in 1943 by Henschel to serve as a replacement for the Tiger I.",
                        210_000,
                        88,
                        185,
                        38,
                        5),
                new TankSeed(nations.get("German Reich"),
                        tankRoles.get("Heavy tank"),
                        "Tiger H1",
                        "/img/tank/tigerh1.png",
                        "The Panzerkampfwagen VI Ausführung H1 (Tiger H1) is the first (early-production) variant of the Tiger I heavy tank family, designed and built by Henschel and used by the German Army during World War II.",
                        105_000,
                        88,
                        100,
                        45,
                        5),
                new TankSeed(nations.get("German Reich"),
                        tankRoles.get("Medium tank"),
                        "Panther A",
                        "/img/tank/panther_a.png",
                        "The Panzerkampfwagen V Ausführung A (Panther A) (Sd.Kfz. Index: Sd.Kfz. 171) is the second production variant of the iconic Panzerkampfwagen V Panther medium tank family.",
                        155_000,
                        75,
                        100,
                        46,
                        5),
                new TankSeed(nations.get("USA"),
                        tankRoles.get("Heavy tank"),
                        "M4A3E2",
                        "/img/tank/sherman_jumbo.png",
                        "The M4A3E2 Sherman - Assault Tank is an armoured modification of the M4A3, which is the fourth variant of the early-generation Medium Tank M4 (Sherman) family.",
                        105_000,
                        75,
                        177,
                        35,
                        5),
                new TankSeed(nations.get("Great Britain"),
                        tankRoles.get("Medium tank"),
                        "Centurion Mk 3",
                        "/img/tank/cent_mk3.png",
                        "The Centurion Mk 3 was the third variant of the Centurion medium tank family. The 84 mm Ordnance QF 20-pounder tank gun was installed in this variant, which provided significantly superior accuracy thanks to a newly developed two-plane fully automatic stabilization system (modified from the Centurion Mk 2).",
                        210_000,
                        84,
                        152,
                        35,
                        4)
        );

        tankRepository.saveAll(toCheck.stream()
                .filter(seed -> !tankRepository.existsByName(seed.name()))
                .map(seed -> Tank.builder()
                        .nation(seed.nation())
                        .tankRole(seed.role())
                        .name(seed.name())
                        .imgPath(seed.imgPath())
                        .description(seed.description())
                        .price(seed.price())
                        .mainGunCalibre(seed.mainGunCalibre())
                        .armorThickness(seed.armorThickness())
                        .maxSpeed(seed.maxSpeed())
                        .crewSize(seed.crewSize())
                        .build())
                .toList());
    }
}
