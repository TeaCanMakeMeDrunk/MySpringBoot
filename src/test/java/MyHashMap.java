import org.apache.jasper.tagplugins.jstl.core.If;

import java.util.HashMap;

public class MyHashMap{
    //建立静态内部类
    static class Node{
        int hash;
        int key;
        int val;
        Node next;

        public Node(int hash, int key, int val){
            this.hash = hash;
            this.key = key;
            this.val = val;
        }
    }

    //默认初始化长度为16
    private final int capacity = 16;
    //定义数组
    private Node[] table;
    //负载因子
    private final float loadFactor = 0.75F;
    //阈值
    private int threshold;
    //记录map的大小
    private int size = 0;

    /**
     * 初始化
     */
    public MyHashMap(){
        table  = new Node[capacity];
        this.threshold = (int) (capacity*loadFactor);
    }

    public void put(int key, int val){
        int index = getTableIndex(key);
        Node temp = table[index];
        if(temp == null ){
            table[index] = new Node(hash(key), key,val);
        }else{
            Node pre = temp;
            while(temp!=null){
                //如果找到相同key的结点
                if(temp.hash == hash(key) && temp.key == key ){
                    temp.val = val;
                    return;
                }
                pre = temp;
                temp = temp.next;
            }
            pre.next = new Node(hash(key),key,val);
        }
        //如果大于 capacity*loadFactor  就要进行扩容
        if( ++size > threshold ){
            resize();
        }
        //如果链表长度大于8并且数组长度大于64 转换为红黑树..
    }

    /**
     * 扩容机制
     */
    private void resize() {
        //首先创建一个2*capacity的数组
        Node[] newTable = new Node[table.length << 1];
        int n = newTable.length;
        //再把就table里面的树reHash到新数组
        for(Node node : table){
            Node temp = node;
            while(temp!=null){
                Node newNode = newTable[ hash(temp.key)%(n-1)];
                if(newNode == null){
                    newTable[ hash(temp.key)%(n-1)] = temp;
                }else{
                    //找到空位插入
                    while(newNode.next!=null){
                        newNode = newNode.next;
                    }
                    newNode.next = temp;
                }
                temp = temp.next;
            }
        }
        table = newTable;
        threshold = (int) (n*loadFactor);
    }

    /**
     * 获得在hash表中的索引
     */
    private int getTableIndex(int key){
        return hash(key)%table.length;
    }

    private int hash(Object key) {
        int h = key.hashCode();
        //^ 异或  不同为1，相同为0
        //h >>> 16是用来取出h的高16，减少碰撞
        return h ^ (h >>> 16);
    }

    public int get(int key){
        int index = getTableIndex(key);
        Node temp = table[index];
        if( temp!=null ) {
            //遍历链表 找到key = key的结点，返回其值
            //假设是链表,未转换为红黑树
            while(temp!= null){
                //应该找到hashCode与equals都相等
                if(temp.hash == hash(key) && temp.key == key ){
                    return temp.val;
                }
                temp = temp.next;
            }
        }
        return -1;
    }

    public static void main(String[] args) {
        MyHashMap mMap = new MyHashMap();
        mMap.put(1,2);
        mMap.put(1,3);
        System.out.println(mMap.get(1));
        for(int i=0; i<16; i++){
            mMap.put(i,i);
        }
        System.out.println(mMap.get(1));
    }
}
