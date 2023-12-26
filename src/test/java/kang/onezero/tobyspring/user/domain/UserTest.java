package kang.onezero.tobyspring.user.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class UserTest {
    User user;
    @BeforeEach
    public void setUp() {
        user = new User();
    }

    @Test
    public void upgradeLevel() {
        Level[] levels = Level.values();
        for (Level level: levels) {
            if (level.nextLevel() == null) continue;
            user.setLevel(level);
            user.upgradeLevel();
            assertThat(user.getLevel()).isEqualTo(level.nextLevel());
        }
    }

    @Test
    public void cannotUpgradeLevel() {
        Level[] levels = Level.values();
        assertThatThrownBy(() -> {
            for (Level level: levels) {
                if (level.nextLevel() != null) continue;
                user.setLevel(level);
                user.upgradeLevel();
            }
        }).isInstanceOf(IllegalStateException.class);
    }
}