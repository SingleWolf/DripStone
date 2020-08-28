package com.walker.webview.commands;

import android.content.Context;

import com.google.auto.service.AutoService;
import com.walker.webview.command.Command;
import com.walker.webview.command.ResultBack;
import com.walker.webview.utils.WebConstants;

import java.util.HashMap;
import java.util.Map;

@AutoService({Command.class})
public class LoginCommand implements Command {
    @Override
    public String name() {
        return "login";
    }

    @Override
    public void exec(Context context, Map params, ResultBack resultBack) {
        try {
            Thread.sleep(2 * 1000);
        } catch (Exception e) {
            e.toString();
        } finally {
            HashMap<String,String> map=new HashMap<>();
            map.put("accountName","walker");
            resultBack.onResult(WebConstants.SUCCESS, name(), map);
        }
    }
}
