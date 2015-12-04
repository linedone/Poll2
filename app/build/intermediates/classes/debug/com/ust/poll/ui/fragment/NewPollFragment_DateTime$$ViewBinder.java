// Generated code from Butter Knife. Do not modify!
package com.ust.poll.ui.fragment;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class NewPollFragment_DateTime$$ViewBinder<T extends com.ust.poll.ui.fragment.NewPollFragment_DateTime> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findOptionalView(source, 2131493995, null);
    target.txt_title = finder.castView(view, 2131493995, "field 'txt_title'");
    view = finder.findRequiredView(source, 2131494007, "method 'fnNewPoll'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.fnNewPoll(p0);
        }
      });
  }

  @Override public void unbind(T target) {
    target.txt_title = null;
  }
}
