package com.mobile.base.controller.base;

import com.mobile.base.security.BaseJwt;

public abstract class BaseController {

    protected Long getCurrentUserId() {
        return BaseJwt.getCurrentUserId();
    }

    protected String getCurrentUsername() {
        return BaseJwt.getCurrentUsername();
    }

    protected String getCurrentEmail() {
        return BaseJwt.getCurrentEmail();
    }

    protected Boolean getIsSuperAdmin() {
        return BaseJwt.getIsSuperAdmin();
    }
}
