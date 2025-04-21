package study

import io.kotest.matchers.equality.shouldBeEqualToComparingFields
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

class DSLTest {
    @ValueSource(strings = ["경수현", "남기원"])
    @ParameterizedTest
    fun name_test(name: String) {
        val person =
            introduce {
                name(name)
            }
        person.name shouldBe name
    }

    @Test
    fun introduce_test() {
        val person =
            introduce {
                name("경수현")
                company("HMC")
                skills {
                    soft("A passion for problem solving")
                    soft("Good communication skills")
                    hard("Kotlin")
                }
                languages {
                    "Korean" level 5
                    "English" level 3
                }
            }
        person.name shouldBe "경수현"
        person.company shouldBe "HMC"
        person.skills[0] shouldBeEqualToComparingFields Skill("A passion for problem solving", "soft")
        person.skills[1] shouldBeEqualToComparingFields Skill("Good communication skills", "soft")
        person.skills[2] shouldBeEqualToComparingFields Skill("Kotlin", "hard")
        person.skills.size shouldBe 3
        person.languages[0] shouldBeEqualToComparingFields Language("Korean", 5)
        person.languages[1] shouldBeEqualToComparingFields Language("English", 3)
        person.languages.size shouldBe 2
    }
}
