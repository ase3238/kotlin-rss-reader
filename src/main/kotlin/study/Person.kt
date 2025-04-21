package study

import kotlin.apply

// `Person.`을 붙이면서, Person 내에 있는 함수만 받을수 있게 됨
// fun introduce(function: Person.()-> Unit) = Person()
//    .apply {
//        function()
//    }

fun introduce(block: PersonBuilder.() -> Unit) = PersonBuilder().apply(block).build() // apply 함수 Param과 동일하여 바로 넘겨줌

class Person(
    val name: String,
    val company: String,
    val skills: List<Skill>,
    val languages: List<Language>,
)

class Skill(
    val name: String,
    val type: String,
)

class Language(
    val name: String,
    val level: Int,
)

class PersonBuilder(
    var name: String = "",
    var company: String = "",
    var skills: MutableList<Skill> = mutableListOf(),
    var languages: MutableList<Language> = mutableListOf(),
    // 불변으로 처리를 하기 위해서는... 별도 Builder가 필요함
) {
    fun name(name: String) {
        this.name = name
    }

    fun company(company: String) {
        this.company = company
    }

    fun skills(function: SkillListBuilder.() -> Unit) {
        SkillListBuilder(this.skills).apply(function)
    }

    fun languages(block: LanguageBuilder.() -> Unit) {
        LanguageBuilder(this.languages).apply(block)
    }

    fun build(): Person {
        return Person(name, company, skills, languages)
    }
}

class LanguageBuilder(
    var languages: MutableList<Language>,
) {
    infix fun String.level(level: Int) {
        languages.add(Language(this, level))
    }
}

class SkillListBuilder(
    var skills: MutableList<Skill>,
) {
    fun soft(name: String) {
        skills.add(Skill(name, "soft"))
    }

    fun hard(name: String) {
        skills.add(Skill(name, "hard"))
    }
}
