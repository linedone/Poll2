// Generated code from Butter Knife. Do not modify!
package com.ust.poll.ui.fragment;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class PickFriendFragment$$ViewBinder<T extends com.ust.poll.ui.fragment.PickFriendFragment> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131493993, "field 'btnSubmitFriendList' and method 'fnPickFriendSubmit'");
    target.btnSubmitFriendList = finder.castView(view, 2131493993, "field 'btnSubmitFriendList'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.fnPickFriendSubmit(p0);
        }
      });
  }

  @Override public void unbind(T target) {
    target.btnSubmitFriendList = null;
  }
}
