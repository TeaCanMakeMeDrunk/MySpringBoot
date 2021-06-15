public class MyBitSet {
    // 使用位存储，numbers中的每个数字转化为二进制bit流，每一位为1对应QQ号为该位的存在
    private long[] words; //一个long变量长度是64位，每个位的值可以是0或者1，这样一个long类型变量最多就可以对64个数字（或者其他数据）进行标志。

    //无参构造
    public MyBitSet() {
    }

    // 根据存储数量的多少，可以对numbers数组进行大小的设置
    public MyBitSet(int length) { //length是65的话 long就有long[0] 和 long[1]
        words = new long[((length-1) >> 6) +1]; // 64的整数倍,确保在不能被64整除的情况下，得到正确的words数组长度
    }

    public void put(int bitIndex) {
        int wordIndex = bitIndex >> 6; //看在words数组的哪里
        //中间还有确保大小在合适范围的
        words[wordIndex] |= (1L << bitIndex); //对1左移 bitIndex 位， bitIndex标志为存在  由高到低，比如words[0]中, 10..0(63个0) 表示64有了
    }

    public boolean get(int bitIndex) {
        int wordIndex = bitIndex >> 6;
        //中间还有确保大小在合适范围的
        return (words[wordIndex] & (1L << bitIndex)) != 0;
    }

    public static void main(String[] args) {
        int[] nums = new int[]{0,1,7,12,22,12, 65, 65};
        MyBitSet qqSet = new MyBitSet(65);
        for(int i: nums){
            if(qqSet.get(i)){
                System.out.println(i);
            }
            qqSet.put(i);
        }
    }
}