// Generated code from Butter Knife. Do not modify!
package com.ust.poll.ui.fragment;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class FriendListFragment$$ViewBinder<T extends com.ust.poll.ui.fragment.FriendListFragment> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131493994, "field 'listView'");
    target.listView = finder.castView(view, 2131493994, "field 'listView'");
  }

  @Override public void unbind(T target) {
    target.listView = null;
  }
}
