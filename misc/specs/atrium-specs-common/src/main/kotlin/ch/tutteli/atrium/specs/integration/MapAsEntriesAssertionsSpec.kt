package ch.tutteli.atrium.specs.integration

import ch.tutteli.atrium.api.fluent.en_GB.*
import ch.tutteli.atrium.api.verbs.internal.expect
import ch.tutteli.atrium.creating.Expect
import ch.tutteli.atrium.specs.*
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.Suite

abstract class MapAsEntriesAssertionsSpec(
    asEntriesFeature: Feature0<Map<String, Int>, Set<Map.Entry<String, Int>>>,
    asEntries: Fun1<Map<String, Int>, Expect<Set<Map.Entry<String, Int>>>.() -> Unit>,
    describePrefix: String = "[Atrium] "
) : Spek({

    include(object : SubjectLessSpec<Map<String, Int>>(
        describePrefix,
        asEntriesFeature.forSubjectLess(),
        asEntries.forSubjectLess { contains("a" to 1) }
    ) {})

    fun describeFun(vararg pairs: SpecPair<*>, body: Suite.() -> Unit) =
        describeFunTemplate(describePrefix, pairs.map { it.name }.toTypedArray(), body = body)

    describeFun(asEntriesFeature, asEntries) {
        val asEntriesFunctions = unifySignatures(asEntriesFeature, asEntries)

        asEntriesFunctions.forEach{ (name, asEntriesFun, _) ->
            it("$name - transformation can be applied and an assertion made") {
                expect(mapOf("a" to 1, "b" to 2)).asEntriesFun {
                    contains.inAnyOrder.only.entries(
                        { isKeyValue("b", 2) },
                        {
                            key { startsWith("a") }
                            value.isGreaterThanOrEqual(1)
                        }
                    )
                }
            }
        }
    }
})
