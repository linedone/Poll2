// Generated code from Butter Knife. Do not modify!
package com.ust.poll.ui.fragment;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class NewPollFragment$$ViewBinder<T extends com.ust.poll.ui.fragment.NewPollFragment> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131493995, "field 'txt_title'");
    target.txt_title = finder.castView(view, 2131493995, "field 'txt_title'");
    view = finder.findRequiredView(source, 2131493998, "field 'option1' and method 'fnOption'");
    target.option1 = finder.castView(view, 2131493998, "field 'option1'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.fnOption(p0);
        }
      });
    view = finder.findRequiredView(source, 2131493999, "field 'option2' and method 'fnOption'");
    target.option2 = finder.castView(view, 2131493999, "field 'option2'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.fnOption(p0);
        }
      });
    view = finder.findRequiredView(source, 2131494000, "field 'option3' and method 'fnOption'");
    target.option3 = finder.castView(view, 2131494000, "field 'option3'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.fnOption(p0);
        }
      });
    view = finder.findRequiredView(source, 2131494001, "field 'option4' and method 'fnOption'");
    target.option4 = finder.castView(view, 2131494001, "field 'option4'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.fnOption(p0);
        }
      });
    view = finder.findRequiredView(source, 2131494002, "method 'fnNewPoll'");
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
    target.option1 = null;
    target.option2 = null;
    target.option3 = null;
    target.option4 = null;
  }
}
