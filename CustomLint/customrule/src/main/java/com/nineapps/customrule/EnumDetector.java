package com.nineapps.customrule;

import com.android.tools.lint.detector.api.Category;
import com.android.tools.lint.detector.api.Detector;
import com.android.tools.lint.detector.api.Implementation;
import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.JavaContext;
import com.android.tools.lint.detector.api.Scope;
import com.android.tools.lint.detector.api.Severity;
import com.intellij.psi.JavaElementVisitor;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiEnumConstant;
import com.intellij.psi.PsiEnumConstantInitializer;

import java.util.Collections;
import java.util.List;

/**
 * Created by zhangzhiyi on 2017/10/8.
 */

public class EnumDetector extends Detector implements Detector.JavaPsiScanner {

    public static final Issue ISSUE = Issue.create("EnumDetector",
            "You should not use Enum",
            "===========================",
            Category.PERFORMANCE,
            9,
            Severity.ERROR,
            new Implementation(EnumDetector.class,
                    Scope.JAVA_FILE_SCOPE));


    @Override
    public List<Class<? extends PsiElement>> getApplicablePsiTypes() {
        return Collections.<Class<? extends PsiElement>>singletonList(PsiEnumConstant.class);
    }

    @Override
    public JavaElementVisitor createPsiVisitor(JavaContext context) {
        return new EnumVisitor(context);
    }

    static class EnumVisitor extends JavaElementVisitor {
        JavaContext mJavaContext;
        public EnumVisitor(JavaContext context) {
            mJavaContext = context;
        }

        @Override
        public void visitEnumConstant(PsiEnumConstant enumConstant) {
            super.visitEnumConstant(enumConstant);
            mJavaContext.report(ISSUE, mJavaContext.getLocation(enumConstant), "Use Annotation replace Enum");
        }

        @Override
        public void visitEnumConstantInitializer(PsiEnumConstantInitializer enumConstantInitializer) {
            super.visitEnumConstantInitializer(enumConstantInitializer);

        }
    }

}
