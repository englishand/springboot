package com.zhy.test;

/**
 * 数据结构：
 *     1.数组：插入快，查找慢，删除慢
 *     2.栈：只能在一端进行插入和删除操作的线性表。先进后出原则存储数据。栈具有记忆作用，对栈的插入和删除操作中，不需要改变栈底指针。可以利用栈实现字符串
 *         反转。
 *     3.队列：一种线性表，只允许表的前端进行删除操作，表的后端进行插入操作。
 *     4.链表:单向链表、双端链表（增加了对尾节点的引用）、双向链表，如果想找到某个节点，必须从第一个节点开始遍历。
 *     5.树：查找、插入、删除效率块。时间复杂度为O(logN),底数是2。N为二叉树节点的总数。
 *     6.哈希表：哈希函数：把一个范围大的数字哈希（转化）成一个小范围的数字，这个小范围的数对应着数组的下标。使用哈希函数向数组插入数据后，这个数组就是哈
 *         希表。
 *         桶：在每个数据项中使用子数组，不是链表，这样的数组称为桶。
 *     7.堆：它是完全二叉树，通常用数组来实现，堆中每一个节点的关键字都大于或等于这个子节点的关键字。
 *     8.图
 */

/**
 * 算法：排序算法、递归算法（调用自身的编程）
 */
public class AlgorithmTest {

    private int[] array = {3,1,4,5,2,7,10,6,9,8};

    public void display(int c){
        System.out.print("第"+(c+1)+"次排序结果为：");
        for (int i=0;i<array.length;i++){
            System.out.print(array[i]+"\t");
        }
        System.out.println();
    }

    /**
     * 冒泡排序，两两比较交换，进行n-1轮排序,时间级别：O(N^2)
     */
    public void testBubbleSort(){
        for (int i=0;i<array.length-1;i++){

            for (int j=i+1;j<array.length;j++){
                int temp =0;
                if (array[i]>array[j]){
                    temp = array[i];
                    array[i] = array[j] ;
                    array[j] = temp;
                }
            }
            display(i);
        }
    }

    /**
     * 选择排序：从待排序的数据元素中选出最小的一个，存放在序列的起始未知，直到元素排完,时间复杂度：O(N^2)
     */
    public void testChoiceSort(){
        for (int i=0;i<array.length-1;i++){
            int min=i;
            for (int j=i+1;j<array.length;j++){
                if (array[min]>=array[j]){
                    min = j;
                }
            }
            if (min!=i){
                int temp = array[i];
                array[i] = array[min];
                array[min] = temp;
            }

            display(i);
        }
    }


    /**
     * 插入排序：将待排的元素插入到已经排好的序列中。(将待排元素与已排好序列的最后一个元素比较，边比较边移动)，时间复杂度：O(N^2)
     */
    public void testInsertSort(){
        int j;//已排好元素的下标+1
        for (int i=1;i<array.length;i++){
            int tem = array[i];//要插入的元素
            j = i;
            while (j>0 && tem<array[j-1]){
                array[j] = array[j-1];
                j--;
            }
            array[j] = tem;

            display(i-1);
        }
    }






    public static void main(String[] args){
        AlgorithmTest test = new AlgorithmTest();
//        test.testBubbleSort();
//        test.testChoiceSort();
        test.testInsertSort();
    }
}
