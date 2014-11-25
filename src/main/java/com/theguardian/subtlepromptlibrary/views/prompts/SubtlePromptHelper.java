package com.theguardian.subtlepromptlibrary.views.prompts;

import android.content.Context;
import android.view.View;
import android.widget.AbsListView;
import android.widget.RelativeLayout;

import com.theguardian.subtlepromptlibrary.R;

public class SubtlePromptHelper {

    public static BaseSubtlePrompt getPromptOnExpandableList(Context context) {
        View promptHeader = View.inflate(context, R.layout.subtle_prompt_layout, null);
        promptHeader.setLayoutParams(new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, AbsListView.LayoutParams.WRAP_CONTENT));
        BaseSubtlePrompt baseSubtlePrompt = (BaseSubtlePrompt) promptHeader.findViewById(R.id.prompt_root_layout);
        baseSubtlePrompt.initFields();

        return baseSubtlePrompt;
    }

    public static BaseSubtlePrompt getPromptOnView(Context context) {
        View promptView = View.inflate(context, R.layout.subtle_prompt_layout, null);
        promptView.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT));

        BaseSubtlePrompt baseSubtlePromptWithImage = (BaseSubtlePrompt) promptView.findViewById(R.id.prompt_root_layout);
        baseSubtlePromptWithImage.initFields();
        baseSubtlePromptWithImage.setVisibility(View.GONE);

        return baseSubtlePromptWithImage;
    }
}
