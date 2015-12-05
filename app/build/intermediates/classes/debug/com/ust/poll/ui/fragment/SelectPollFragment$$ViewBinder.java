// Generated code from Butter Knife. Do not modify!
package com.ust.poll.ui.fragment;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class SelectPollFragment$$ViewBinder<T extends com.ust.poll.ui.fragment.SelectPollFragment> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findOptionalView(source, 2131493995, null);
    target.txt_title = finder.castView(view, 2131493995, "field 'txt_title'");
    view = finder.findRequiredView(source, 2131494025, "field 'btn_select_op1'");
    target.btn_select_op1 = finder.castView(view, 2131494025, "field 'btn_select_op1'");
    view = finder.findRequiredView(source, 2131494026, "field 'btn_select_op2'");
    target.btn_select_op2 = finder.castView(view, 2131494026, "field 'btn_select_op2'");
    view = finder.findRequiredView(source, 2131494027, "field 'btn_select_op3'");
    target.btn_select_op3 = finder.castView(view, 2131494027, "field 'btn_select_op3'");
    view = finder.findRequiredView(source, 2131494028, "field 'btn_select_op4'");
    target.btn_select_op4 = finder.castView(view, 2131494028, "field 'btn_select_op4'");
  }

  @Override public void unbind(T target) {
    target.txt_title = null;
    target.btn_select_op1 = null;
    target.btn_select_op2 = null;
    target.btn_select_op3 = null;
    target.btn_select_op4 = null;
  }
}
