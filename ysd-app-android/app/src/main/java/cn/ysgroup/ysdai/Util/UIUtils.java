package cn.ysgroup.ysdai.Util;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;


public class UIUtils {




    public static void addEnabled(final EditText[] ets, final View targetView, final String[] regexs) {
        if (null == ets || null == regexs
                || ets.length != regexs.length || null == targetView) {
            return;
        }
        final int len = ets.length;
        for (int i = 0; i < len; i++) {
            final EditText et = ets[i];
            final int index = i;
            et.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    boolean b = StringUtils.matchRegex(s.toString(), regexs[index]);
                    et.setTag(et.getId(), b);

                    boolean targetEnabled = true;

                    for (int i = 0; i < len; i++) {
                        Object obj = ets[i].getTag(ets[i].getId());

                        if (null == obj || (obj instanceof Boolean && !((Boolean) obj).booleanValue())) {
                            targetEnabled = false;
                            break;
                        }

                    }

                    if (targetEnabled) {
                        targetView.setEnabled(true);
                    } else {
                        targetView.setEnabled(false);
                    }

                }
            });
        }
    }


}
