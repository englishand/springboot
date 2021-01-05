package com.zhy.test.utils;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * 内部类修改类的属性值
 */
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
                System.out.println("按钮被按下");
                System.out.println("原来city的值为：" + city);
                city = "beijing";
            }
        });
    }

    public static void main(String[] args) {
        InnerClassDemo a = new InnerClassDemo();
        a.addActionListener();
        a.btn.doClick();
        System.out.println("现在city的值为："+a.city);
    }
}
