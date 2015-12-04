// Generated code from Butter Knife. Do not modify!
package com.ust.poll.activity;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class LoginActivity$$ViewBinder<T extends com.ust.poll.activity.LoginActivity> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131494029, "field 'txt_phone_no'");
    target.txt_phone_no = finder.castView(view, 2131494029, "field 'txt_phone_no'");
    view = finder.findRequiredView(source, 2131494030, "field 'txt_captcha_code'");
    target.txt_captcha_code = finder.castView(view, 2131494030, "field 'txt_captcha_code'");
    view = finder.findRequiredView(source, 2131494031, "field 'imgView'");
    target.imgView = finder.castView(view, 2131494031, "field 'imgView'");
  }

  @Override public void unbind(T target) {
    target.txt_phone_no = null;
    target.txt_captcha_code = null;
    target.imgView = null;
  }
}
