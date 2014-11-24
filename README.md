android-subtle-prompts
======================

Library which enables the creation of user information prompts similar to those in the Guardian Android app

![ScreenShot](sample_small.png?raw=true "Subtle Prompt on view")

Usage
--------

Call SubtlePromptHelper to get an inflated view with a subtle prompt.

    BaseSubtlePrompt baseSubtlePrompt = SubtlePromptHelper.getPromptOnExpandableList(context);
    baseSubtlePrompt.setAnimation(listener);

To add a popping button animated with the subtle prompt, for that, call setAnimation with the
view to animate, and set the standard and popping drawables to use. 

    baseSubtlePrompt.setAnimation(animatedView, listener);
    baseSubtlePrompt.setPromptTitle(promptTitle);
    baseSubtlePrompt.setPoppingIcon(poppingIconDrawable);
    baseSubtlePrompt.setStandardIcon(standardIconDrawable);

To add animage to the prompt as the one on the example, set the fields promptImage and bodyText.

    baseSubtlePrompt.setBodyText(bodyString;
    baseSubtlePrompt.setPromptImage(promptDrawable);

Don't forget to add the drawable for close button

    baseSubtlePrompt.setPromptCloseButton(closeDrawable);

Your listener controls what happen when the subtle prompt is closed

    new BaseSubtlePrompt.Listener() {
        @Override
        public void onCollapseEnd() {
            // Do something when the subtlePrompt is dismissed
        }
    });


ProGuard
--------

If you are using ProGuard you might need to add the following option:
```
-dontwarn com.guardian.subtlepromptlibrary.**
```

License
--------

    Copyright 2014 Guardians News and Media Limited

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.



