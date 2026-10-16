package com.teecclesia.detekt.rules

import io.gitlab.arturbosch.detekt.api.CodeSmell
import io.gitlab.arturbosch.detekt.api.Config
import io.gitlab.arturbosch.detekt.api.Debt
import io.gitlab.arturbosch.detekt.api.Entity
import io.gitlab.arturbosch.detekt.api.Issue
import io.gitlab.arturbosch.detekt.api.Rule
import io.gitlab.arturbosch.detekt.api.Severity
import org.jetbrains.kotlin.psi.KtImportDirective
import org.jetbrains.kotlin.psi.KtPackageDirective
import org.jetbrains.kotlin.psi.KtUserType
import org.jetbrains.kotlin.psi.psiUtil.getParentOfType

class NoFullyQualifiedNameRule(config: Config = Config.empty) : Rule(config) {

    override val issue = Issue(
        id = "NoFullyQualifiedName",
        severity = Severity.CodeSmell,
        description = "Fully qualified names are strictly forbidden inline. Always import at the top of the file.",
        debt = Debt.FIVE_MINS
    )

    override fun visitUserType(type: KtUserType) {
        super.visitUserType(type)

        // Ignore package and import directives
        if (type.getParentOfType<KtPackageDirective>(true) != null ||
            type.getParentOfType<KtImportDirective>(true) != null
        ) {
            return
        }

        // Only report the top-most qualifier to avoid duplicate reports for deep nested types
        if (type.qualifier != null && type.parent !is KtUserType) {
            val rootQualifier = type.getRootQualifier()
            val rootName = rootQualifier.referencedName ?: return
            if (rootName.firstOrNull()?.isLowerCase() == true) {
                report(
                    CodeSmell(
                        issue = issue,
                        entity = Entity.from(type),
                        message = "Avoid inline fully qualified name '${type.text}'. Import it at the top of the file."
                    )
                )
            }
        }
    }

    private fun KtUserType.getRootQualifier(): KtUserType {
        var current = this
        while (current.qualifier != null) {
            current = current.qualifier ?: break
        }
        return current
    }
}
