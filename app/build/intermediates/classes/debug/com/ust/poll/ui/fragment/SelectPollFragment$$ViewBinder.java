// Generated code from Butter Knife. Do not modify!
package com.ust.poll.ui.fragment;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class SelectPollFragment$$ViewBinder<T extends com.ust.poll.ui.fragment.SelectPollFragment> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findOptionalView(source, 2131494005, null);
    target.txt_title = finder.castView(view, 2131494005, "field 'txt_title'");
    view = finder.findRequiredView(source, 2131494037, "field 'btn_select_op1' and method 'onClick'");
    target.btn_select_op1 = finder.castView(view, 2131494037, "field 'btn_select_op1'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.onClick(p0);
        }
      });
    view = finder.findRequiredView(source, 2131494038, "field 'btn_select_op2' and method 'onClick'");
    target.btn_select_op2 = finder.castView(view, 2131494038, "field 'btn_select_op2'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.onClick(p0);
        }
      });
    view = finder.findRequiredView(source, 2131494039, "field 'btn_select_op3' and method 'onClick'");
    target.btn_select_op3 = finder.castView(view, 2131494039, "field 'btn_select_op3'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.onClick(p0);
        }
      });
    view = finder.findRequiredView(source, 2131494040, "field 'btn_select_op4' and method 'onClick'");
    target.btn_select_op4 = finder.castView(view, 2131494040, "field 'btn_select_op4'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.onClick(p0);
        }
      });
  }

  @Override public void unbind(T target) {
    target.txt_title = null;
    target.btn_select_op1 = null;
    target.btn_select_op2 = null;
    target.btn_select_op3 = null;
    target.btn_select_op4 = null;
  }
}
