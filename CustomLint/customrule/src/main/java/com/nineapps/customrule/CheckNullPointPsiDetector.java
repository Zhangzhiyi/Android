package com.nineapps.customrule;

import com.android.annotations.NonNull;
import com.android.annotations.Nullable;
import com.android.tools.lint.client.api.JavaEvaluator;
import com.android.tools.lint.detector.api.Category;
import com.android.tools.lint.detector.api.Detector;
import com.android.tools.lint.detector.api.Implementation;
import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.JavaContext;
import com.android.tools.lint.detector.api.Scope;
import com.android.tools.lint.detector.api.Severity;
import com.intellij.psi.JavaElementVisitor;
import com.intellij.psi.JavaRecursiveElementVisitor;
import com.intellij.psi.PsiBinaryExpression;
import com.intellij.psi.PsiCallExpression;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiExpression;
import com.intellij.psi.PsiExpressionList;
import com.intellij.psi.PsiIfStatement;
import com.intellij.psi.PsiJavaToken;
import com.intellij.psi.PsiLiteralExpression;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiMethodCallExpression;
import com.intellij.psi.PsiReferenceExpression;

import java.util.Arrays;
import java.util.List;

/**
 * Created by zhangzhiyi on 2017/5/26.
 */

public class CheckNullPointPsiDetector extends Detector implements Detector.JavaPsiScanner {

    public final static String FRAGMENT_V4_CLS = "android.support.v4.app.Fragment";
    public final static String FRAGMENT_CLS = "android.app.Fragment";

    public static final Issue ISSUE = Issue.create("CheckNullPoint",
            "Null Point",
            "===========================",
            Category.CORRECTNESS,
            9,
            Severity.ERROR,
            new Implementation(CheckNullPointPsiDetector.class,
                    Scope.JAVA_FILE_SCOPE));


    @Override
    public List<String> getApplicableMethodNames() {
        return Arrays.asList("getArguments");
    }

    @Override
    public void visitMethod(JavaContext context, JavaElementVisitor visitor, PsiMethodCallExpression call, PsiMethod method) {
        JavaEvaluator evaluator = context.getEvaluator();
        if (method.getName().equals("getArguments") && !evaluator.isMemberInClass(method, FRAGMENT_V4_CLS) && !evaluator.isMemberInClass(method, FRAGMENT_CLS)) {
            return;
        }
        MethodVisitor methodVisitor = new MethodVisitor(context);
//        call.accept(methodVisitor);
        PsiReferenceExpression referenceExpression = call.getMethodExpression();
        PsiElement qualifier = referenceExpression.getQualifier();
        PsiExpressionList argumentList = call.getArgumentList();
        PsiElement nextSibling = call.getNextSibling();
        PsiElement parent = call.getParent();
//        parent.accept(methodVisitor);
        int length = call.getTextLength();
        if (nextSibling != null) {
            boolean flag = isNullCheck(context, parent, call);
        }
    }

    private boolean isNullCheck(@NonNull JavaContext context,
                                @Nullable PsiElement cur,
                                @NonNull PsiMethodCallExpression call) {
        while (cur != null) {
            if (cur instanceof PsiIfStatement) {
                PsiIfStatement ifNode = (PsiIfStatement) cur;
                if (ifNode.getCondition() instanceof PsiBinaryExpression) {
                    PsiBinaryExpression binaryExpression = (PsiBinaryExpression) ifNode.getCondition();
                    if (binaryExpression.getLOperand() instanceof PsiMethodCallExpression || binaryExpression.getROperand() instanceof PsiMethodCallExpression) {

                    }

                    PsiMethodCallExpression conditionCall = (PsiMethodCallExpression) ifNode.getCondition();
                    if (conditionCall.getMethodExpression().getReferenceName().equals("getArguments")) {

                    }
                }
            }
            cur = cur.getParent();
        }

        return false;
    }

    static class MethodVisitor extends JavaRecursiveElementVisitor {
        private final JavaContext mContext;

        public MethodVisitor(JavaContext context) {
            mContext = context;
        }

        @Override
        public void visitMethodCallExpression(PsiMethodCallExpression expression) {
            super.visitMethodCallExpression(expression);
        }

        @Override
        public void visitCallExpression(PsiCallExpression callExpression) {
            super.visitCallExpression(callExpression);
        }

        @Override
        public void visitJavaToken(PsiJavaToken token) {
            super.visitJavaToken(token);
        }

        @Override
        public void visitExpression(PsiExpression expression) {
            super.visitExpression(expression);
        }

        @Override
        public void visitElement(PsiElement element) {
            super.visitElement(element);
        }

        @Override
        public void visitLiteralExpression(PsiLiteralExpression expression) {
            super.visitLiteralExpression(expression);
        }
    }
}
