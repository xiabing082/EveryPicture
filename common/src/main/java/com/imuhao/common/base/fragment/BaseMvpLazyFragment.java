package com.imuhao.common.base.fragment;


import android.os.Bundle;
import android.view.View;

import com.imuhao.common.base.mvp.BaseModel;
import com.imuhao.common.base.mvp.BasePresenter;
import com.imuhao.common.utils.ClassUtils;


public abstract class BaseMvpLazyFragment<M extends BaseModel, P extends BasePresenter>
		extends BaseLazyFragment {

	private M model;
	public P presenter;

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		model = ClassUtils.getT(this, 0);
		presenter = ClassUtils.getT(this, 1);
		presenter.attachModelView(model, this);

		super.onViewCreated(view, savedInstanceState);
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		if (presenter != null) {
			presenter.detachView();
		}
	}
}
