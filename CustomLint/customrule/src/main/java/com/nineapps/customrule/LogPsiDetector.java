package com.nineapps.customrule;

import com.android.tools.lint.client.api.JavaEvaluator;
import com.android.tools.lint.detector.api.Category;
import com.android.tools.lint.detector.api.Detector;
import com.android.tools.lint.detector.api.Implementation;
import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.JavaContext;
import com.android.tools.lint.detector.api.Scope;
import com.android.tools.lint.detector.api.Severity;
import com.intellij.psi.JavaElementVisitor;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiMethodCallExpression;

import java.util.Arrays;
import java.util.List;

/**
 * Created by zhangzhiyi on 2017/5/7.
 */

public class LogPsiDetector extends Detector implements Detector.JavaPsiScanner {

    public static final String LOG_CLS = "android.util.Log";
    public static final String SYSTEM_CLS = "java.lang.System";

    private static final Implementation IMPLEMENTATION = new Implementation(
            LogPsiDetector.class,
            Scope.JAVA_FILE_SCOPE);

    public static final Issue ISSUE = Issue.create(
            "LogUse",
            "Please don't use Log/System.out.println",
            "Debug info should not print in the console",
            Category.SECURITY, 5, Severity.ERROR,
            IMPLEMENTATION);

    @Override
    public List<String> getApplicableMethodNames() {
        return Arrays.asList(
                "d",
                "e",
                "i",
                "v",
                "w",
                "println",
                "print");
    }

    @Override
    public void visitMethod(JavaContext context, JavaElementVisitor visitor, PsiMethodCallExpression call, PsiMethod method) {
        super.visitMethod(context, visitor, call, method);
        JavaEvaluator evaluator = context.getEvaluator();
        if (!evaluator.isMemberInClass(method, LOG_CLS) && !evaluator.isMemberInClass(method, SYSTEM_CLS)) {
            return;
        }

        context.report(ISSUE, context.getLocation(call), "Use LogUtil avoid to print debug info");
    }

}
