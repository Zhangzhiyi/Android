package com.nineapps.customrule;

import com.android.tools.lint.detector.api.Category;
import com.android.tools.lint.detector.api.Context;
import com.android.tools.lint.detector.api.Detector;
import com.android.tools.lint.detector.api.Implementation;
import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.LintUtils;
import com.android.tools.lint.detector.api.Scope;
import com.android.tools.lint.detector.api.Severity;

import java.util.Map;

/**
 * Created by zhangzhiyi on 2017/5/23.
 */

public class GradleVersionDetector extends Detector implements Detector.GradleScanner {

    public final static String GRADLE_VERSION = "2.2.3";
    private static final Implementation IMPLEMENTATION = new Implementation(
            GradleVersionDetector.class,
            Scope.GRADLE_SCOPE);


    public static final Issue ISSUE = Issue.create(
            "GradleVersion",
            "Gradle version haved change",
            "Please don't modify the gradle version!",
            Category.CORRECTNESS,
            4,
            Severity.FATAL,
            IMPLEMENTATION);

    @Override
    public void visitBuildScript(Context context, Map<String, Object> sharedData) {
        if (context.getProject() == context.getMainProject()) {
            if (!context.getProject().getGradleModelVersion().equals(GRADLE_VERSION)) {
                context.report(ISSUE, LintUtils.guessGradleLocation(context.getProject()), "Please don't modify the gradle version!");
            }
        }
    }
}
