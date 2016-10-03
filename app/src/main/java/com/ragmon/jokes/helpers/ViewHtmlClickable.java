package com.ragmon.jokes.helpers;


import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.URLSpan;
import android.util.Log;
import android.view.View;
import android.widget.TextView;


public class ViewHtmlClickable {

    /**
     * On link click listener.
     */
    public static abstract class LinkClickListener {
        public abstract void onClick(View view, URLSpan span);
    }


    /**
     * Make link clickable.
     *
     * @param strBuilder
     * @param span
     */
    public static void makeLinkClickable(SpannableStringBuilder strBuilder, final URLSpan span,
                                         final LinkClickListener clickListener)
    {
        int start = strBuilder.getSpanStart(span);
        int end = strBuilder.getSpanEnd(span);
        int flags = strBuilder.getSpanFlags(span);
        ClickableSpan clickable = new ClickableSpan() {
            public void onClick(View view) {
                clickListener.onClick(view, span);
            }
        };
        strBuilder.setSpan(clickable, start, end, flags);
        strBuilder.removeSpan(span);
    }

    /**
     * Set TextView Html and bind links click listener.
     *
     * @param text
     * @param html
     * @param clickListener
     */
    public static void setTextViewHTML(TextView text, String html, LinkClickListener clickListener)
    {
        CharSequence sequence = Html.fromHtml(html);
        SpannableStringBuilder strBuilder = new SpannableStringBuilder(sequence);
        URLSpan[] urls = strBuilder.getSpans(0, sequence.length(), URLSpan.class);
        for(URLSpan span : urls) {
            makeLinkClickable(strBuilder, span, clickListener);
        }
        text.setText(strBuilder);
        text.setMovementMethod(LinkMovementMethod.getInstance());
    }

}
