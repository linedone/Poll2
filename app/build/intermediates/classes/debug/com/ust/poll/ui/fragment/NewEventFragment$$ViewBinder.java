// Generated code from Butter Knife. Do not modify!
package com.ust.poll.ui.fragment;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class NewEventFragment$$ViewBinder<T extends com.ust.poll.ui.fragment.NewEventFragment> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131493980, "field 'txt_etitle'");
    target.txt_etitle = finder.castView(view, 2131493980, "field 'txt_etitle'");
    view = finder.findRequiredView(source, 2131493981, "field 'txt_eDate' and method 'fnPickDate'");
    target.txt_eDate = finder.castView(view, 2131493981, "field 'txt_eDate'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.fnPickDate(p0);
        }
      });
    view = finder.findRequiredView(source, 2131493982, "field 'txt_eTime' and method 'fnPickTime'");
    target.txt_eTime = finder.castView(view, 2131493982, "field 'txt_eTime'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.fnPickTime(p0);
        }
      });
    view = finder.findRequiredView(source, 2131493983, "field 'txt_eVenue'");
    target.txt_eVenue = finder.castView(view, 2131493983, "field 'txt_eVenue'");
    view = finder.findRequiredView(source, 2131493984, "field 'txt_eRemarkURL'");
    target.txt_eRemarkURL = finder.castView(view, 2131493984, "field 'txt_eRemarkURL'");
    view = finder.findRequiredView(source, 2131493986, "field 'btn_event_pick_photo' and method 'fnPickPhoto'");
    target.btn_event_pick_photo = finder.castView(view, 2131493986, "field 'btn_event_pick_photo'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.fnPickPhoto(p0);
        }
      });
    view = finder.findRequiredView(source, 2131493987, "method 'fnPickFriends'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.fnPickFriends(p0);
        }
      });
    view = finder.findRequiredView(source, 2131493988, "method 'fnEventCreate'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.fnEventCreate(p0);
        }
      });
  }

  @Override public void unbind(T target) {
    target.txt_etitle = null;
    target.txt_eDate = null;
    target.txt_eTime = null;
    target.txt_eVenue = null;
    target.txt_eRemarkURL = null;
    target.btn_event_pick_photo = null;
  }
}
