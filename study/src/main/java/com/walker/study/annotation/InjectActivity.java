package com.walker.study.annotation;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.walker.core.base.mvc.BaseActivity;
import com.walker.core.util.ToastUtils;
import com.walker.study.R;
import com.walker.study.proxy.Cat;
import com.walker.study.proxy.IAnimal;
import com.walker.study.proxy.IClimb;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @Author Walker
 * @Date 2020-04-28 00:12
 * @Summary 注解、反射练习
 */
public class InjectActivity extends BaseActivity {

    public static final String KEY_ID = "key_demo_inject_activity";

    @LazyIntent(value = "name")
    private String mName;
    @LazyIntent(value = "age")
    private int mAge;
    @LazyIntent(value = "handsome")
    private boolean mHandsome;

    static class R2 {
        static class id {
            public static final String tvTap1 = "R.id.tvTap1";
            public static final String tvTap2 = "R.id.tvTap2";
            public static final String tvTap3 = "R.id.tvTap3";
        }
    }

    public static void start(Context context, String name, int age, boolean handsome) {
        Intent intent = new Intent(context, InjectActivity.class);
        intent.putExtra("name", name);
        intent.putExtra("age", age);
        intent.putExtra("handsome", handsome);
        if (context instanceof Activity) {
            context.startActivity(intent);
        } else {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }
    }


    @Override
    protected void init(Bundle savedInstanceState) {
        InjectUtils.injectIntent(this);
//        InjectUtils.injectClick(this);
        InjectUtils.injectEvent(this);
        initView();
    }

    private void initView() {
        TextView tvContent = findViewById(R.id.tvContent);
        StringBuilder content = new StringBuilder();
        content.append(String.format("Hello !\nI'm %s\nMy age is %d\n", mName, mAge));
        if (mHandsome) {
            content.append("And I'm handsome\n");
        }
        content.append("Nice to meeting you\n");
        content.append("Thanks\n");
        tvContent.setText(content);
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_study_inject;
    }

    @OnClick(value = {}, idStr = {R2.id.tvTap1, R2.id.tvTap2})
    public void onclick(View v) {
        switch (InjectUtils.findIdName(v.getId())) {
            case R2.id.tvTap1:
                ToastUtils.showCenter("This is tap 1");
                break;
            case R2.id.tvTap2:
                ToastUtils.showCenter("This is tap 2");
                break;
            default:
                break;
        }
    }

    @OnLongClick(value = {}, idStr = {R2.id.tvTap1, R2.id.tvTap2})
    public boolean onLongClick(View v) {
        switch (InjectUtils.findIdName(v.getId())) {
            case R2.id.tvTap1:
                ToastUtils.showCenter("test proxy");
                testProxy();
                break;
            case R2.id.tvTap2:
                ToastUtils.showCenter("This is tap 2 onLongClick");
                break;
            default:
                break;
        }
        return true;
    }

    @OnClick(value = {},idStr = R2.id.tvTap3)
    public void click(View v){
        ToastUtils.showCenter("This is tap 3");
    }

    private void testProxy() {
        Cat cat=new Cat();
        Object proxy=Proxy.newProxyInstance(this.getClassLoader(), new Class[]{IAnimal.class, IClimb.class}, new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                return method.invoke(cat,args);
            }
        });

        IAnimal animal= (IAnimal) proxy;
        animal.eat();

        IClimb climb= (IClimb) proxy;
        climb.climb();
    }
}
