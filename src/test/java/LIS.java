import java.util.List;

public class LIS {
    public static int lengthOfLIS(int[] nums) {
        //dp[i] 表示nums[0,...,i]中最长递增子序列长度
        //动态方程 dp[i] = max(dp[j]) + 1 (\forall j, 0 < j < i, nums[i]>nums[j])
        if(nums.length == 0) return 0;
        int[] dp = new int[nums.length];

        int maxRes = dp[0];
        for(int i = 0; i<nums.length; i++){
            int maxJ = 0; //如果没有 保持dp[i] = 1
            for(int j = 0; j<i; j++){
                if(nums[i] > nums[j]){
                    maxJ = Math.max(maxJ, dp[j]);//找到max(dp[j])
                }
            }
            dp[i] = maxJ + 1;//初始值为1
            maxRes = Math.max(maxRes, dp[i]);
        }
        int[] resNum = new int[maxRes];
        //从后往前推maxRes
        int temp = maxRes;
        int pre = nums.length;
        while(temp!=0){
            //从前往后扫描，给出序列路径
            int end=pre;
            for(int i=0; i<end; i++){
                if (dp[i] == temp){
                    resNum[temp-1] = nums[i];
                    System.out.println("index:" + i);
                    pre = i;
                }
            }
            temp--;
        }
        for(int num : resNum){
            System.out.println(num);
        }
        return maxRes;
    }

    public static void main(String[] args) {
        int[] num1 = new int[]{10,9,2,5,3,7,101,18};
        int[] num2 = new int[]{10,30,20,25,50,30,90,60};
        int[] num3 = new int[]{1,3,1};
        System.out.println(lengthOfLIS(num2));
    }
}
