package com.zhy.utils;

import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * 内部类修改类的属性值
 */
@Slf4j
public class InnerClassDemo {

    String city = "shanghai";
    private JButton btn;

    public InnerClassDemo() {
        btn = new JButton("测试");
    }

    public void addActionListener() {
        btn.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                log.info("按钮被按下");
                log.info("原来city的值为：" + city);
                city = "beijing";
            }
        });
    }

    public static void main(String[] args) {
        InnerClassDemo a = new InnerClassDemo();
        a.addActionListener();
        a.btn.doClick();
        log.info("现在city的值为："+a.city);
    }
}
