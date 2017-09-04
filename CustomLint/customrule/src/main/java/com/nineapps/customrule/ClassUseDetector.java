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
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiMethodCallExpression;

import java.util.Arrays;
import java.util.List;

/**
 * Created by zhangzhiyi on 2017/5/25.
 */

public class ClassUseDetector extends Detector implements Detector.JavaPsiScanner {

    public final static String TIMER_CLS = "java.util.Timer";
    public final static String DIALOG_FAGMENT_CLS = "android.app.DialogFragment";
    public final static String DIALOG_FAGMENT_V4_CLS = "android.support.v4.app.DialogFragment";

    public static final Issue TIMER_ISSUE = Issue.create("TimerNoUse",
            "You should not use Timer class.",
            "You should not use Timer class directly. Instead, you should use android.os.Handler.",
            Category.PERFORMANCE,
            9,
            Severity.ERROR,
            new Implementation(ClassUseDetector.class,
                    Scope.JAVA_FILE_SCOPE));


//    @Override
//    public void checkClass(ClassContext context, ClassNode classNode) {
//        if (classNode.name.equals(TIMER_CLS)) {
//            context.report(TIMER_ISSUE, context.getLocation(classNode), "If you want do something with a timer, you can use android.os.Handler to avoid new Thread instead.");
//        }
//    }


    @Override
    public List<String> applicableSuperClasses() {
        return Arrays.asList(TIMER_CLS, DIALOG_FAGMENT_CLS, DIALOG_FAGMENT_V4_CLS);
    }

    @Override
    public List<String> getApplicableConstructorTypes() {
        return Arrays.asList(TIMER_CLS, DIALOG_FAGMENT_CLS, DIALOG_FAGMENT_V4_CLS);
    }

    @Override
    public void checkClass(JavaContext context, PsiClass declaration) {
        super.checkClass(context, declaration);
        JavaEvaluator evaluator = context.getEvaluator();
        evaluator.extendsClass(declaration, TIMER_CLS, false);
    }

    @Override
    public void visitMethod(JavaContext context, JavaElementVisitor visitor, PsiMethodCallExpression call, PsiMethod method) {
        super.visitMethod(context, visitor, call, method);
    }
}
