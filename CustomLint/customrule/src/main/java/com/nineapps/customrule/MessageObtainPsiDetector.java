package com.nineapps.customrule;

import com.android.tools.lint.detector.api.Category;
import com.android.tools.lint.detector.api.Detector;
import com.android.tools.lint.detector.api.Implementation;
import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.JavaContext;
import com.android.tools.lint.detector.api.Location;
import com.android.tools.lint.detector.api.Scope;
import com.android.tools.lint.detector.api.Severity;
import com.intellij.psi.JavaElementVisitor;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiNewExpression;

import java.util.Arrays;
import java.util.List;

/**
 * Created by zhangzhiyi on 2017/5/25.
 */

public class MessageObtainPsiDetector extends Detector implements Detector.JavaPsiScanner{

    public static final String MESSAGE_CLS = "android.os.Message";

    public static final Issue ISSUE = Issue.create("MessageObtainNotUsed",
            "You should not call `new Message()` directly.",
            "You should not call `new Message()` directly. Instead, you should use `handler.obtainMessage` or `Message.Obtain()`.",
            Category.PERFORMANCE,
            9,
            Severity.ERROR,
            new Implementation(MessageObtainPsiDetector.class,
                    Scope.JAVA_FILE_SCOPE));

    @Override
    public List<String> getApplicableConstructorTypes() {
        return Arrays.asList(MESSAGE_CLS);
    }

    @Override
    public void visitConstructor(JavaContext context, JavaElementVisitor visitor, PsiNewExpression node, PsiMethod constructor) {
        Location location = context.getLocation(node);
        context.report(ISSUE, location, "Please don't call `new Message()` directly. Use `Message.Obtain()` get better performance");
    }
}
