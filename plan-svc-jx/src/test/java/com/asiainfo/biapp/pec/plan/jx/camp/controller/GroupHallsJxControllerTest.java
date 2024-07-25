package com.asiainfo.biapp.pec.plan.jx.camp.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class GroupHallsJxControllerTest {

    @Test
    public void testIsPalindromeEvenLength() {
        // 测试一个长度为偶数的回文字符串
        String str = "abcba";
        boolean result = GroupHallsJxController.isPalindrome(str);
        Assertions.assertTrue(result, "String 'abcba' should be a palindrome.");
    }

    @Test
    public void testIsPalindromeOddLength() {
        // 测试一个长度为奇数的回文字符串
        String str = "abccba";
        boolean result = GroupHallsJxController.isPalindrome(str);
        Assertions.assertTrue(result, "String 'abccba' should be a palindrome.");
    }

    @Test
    public void testIsPalindromeNotPalindrome() {
        // 测试一个非回文字符串
        String str = "abcde";
        boolean result = GroupHallsJxController.isPalindrome(str);
        Assertions.assertFalse(result, "String 'abcde' should not be a palindrome.");
    }

    @Test
    public void testIsPalindromeEmptyString() {
        // 测试空字符串
        String str = "";
        boolean result = GroupHallsJxController.isPalindrome(str);
        Assertions.assertTrue(result, "Empty string should be considered as a palindrome.");
    }

    @Test
    public void testIsPalindromeNull() {
        // 测试 null 输入
        String str = null;
        Assertions.assertThrows(NullPointerException.class, () -> {
            GroupHallsJxController.isPalindrome(str);
        }, "Passing null should throw NullPointerException.");
    }

    // 你可以根据需要添加更多测试用例
}
