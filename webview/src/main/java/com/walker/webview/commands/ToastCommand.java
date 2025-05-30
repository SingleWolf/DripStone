package com.walker.webview.commands;

import android.content.Context;
import android.widget.Toast;

import com.google.auto.service.AutoService;
import com.walker.webview.command.Command;
import com.walker.webview.command.ResultBack;

import java.util.Map;

@AutoService({Command.class})
public class ToastCommand implements Command {
    @Override
    public String name() {
        return "showToast";
    }

    @Override
    public void exec(Context context, Map params, ResultBack resultBack) {
        Toast.makeText(context, String.valueOf(params.get("message")), Toast.LENGTH_SHORT).show();
    }
}
