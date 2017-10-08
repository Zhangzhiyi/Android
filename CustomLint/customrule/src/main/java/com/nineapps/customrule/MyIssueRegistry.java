package com.nineapps.customrule;


import com.android.tools.lint.client.api.IssueRegistry;
import com.android.tools.lint.detector.api.Issue;

import java.util.Arrays;
import java.util.List;

/**
 * Created by zhangzhiyi on 2017/1/12.
 */

public class MyIssueRegistry extends IssueRegistry {
    @Override
    public List<Issue> getIssues() {
        return Arrays.asList(
                LogPsiDetector.ISSUE,
                ConstantNameDetector.ISSUE,
                GradleVersionDetector.ISSUE,
                MessageObtainPsiDetector.ISSUE,
                ClassUseDetector.TIMER_ISSUE,
                CheckNullPointPsiDetector.ISSUE,
                EnumDetector.ISSUE
        );
    }
}
