package logic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.lang.*;
import net.sf.json.*;


public class PrintReceipt {

	protected Map<String, GoodInfo> good_infos_map;	
//	private Map<String, List<PromotionType>> promotion_good_map;
//	private double five_percent_off;
	
	protected String store_name;
	protected String discount_title;
	protected String currency;
	
	public PrintReceipt () {
		good_infos_map = new HashMap<String,GoodInfo>();
		store_name = "<不赚钱商店>";
		discount_title = "批发价出售商品";
		currency = "元";
	}
	
	public PrintReceipt (String goods_info) {
		good_infos_map = new HashMap<String,GoodInfo>();
		store_name = "<不赚钱商店>";
		discount_title = "批发价出售商品";
		currency = "元";
		LoadGoodInfo(goods_info);
	}
	
	public PrintReceipt (String goods_info, String store_name,String discount_title,String currency) {
		good_infos_map = new HashMap<String,GoodInfo>();
		this.store_name = store_name;
		this.discount_title = discount_title;
		this.currency = currency;
		LoadGoodInfo(goods_info);
	}
	
	
	public void LoadGoodInfo(String json){
		try{
			JSONArray json_array = JSONArray.fromObject(json);
			@SuppressWarnings("unchecked")
			List<GoodInfo> good_list = (List<GoodInfo>) 
					JSONArray.toCollection(json_array, GoodInfo.class);
			for (int i = 0; i < good_list.size(); ++i) {
				GoodInfo good = good_list.get(i);	
				good_infos_map.put(good.getBarcode(),good);				
			}
		} catch(JSONException e){
			JSONObject json_obj = JSONObject.fromObject(json);
			GoodInfo good = (GoodInfo) JSONObject.toBean(json_obj, GoodInfo.class);
			good_infos_map.put(good.getBarcode(),good);	
		}

	}

	
	public String ParseInputToJson(List<String> input){
		int num = 1;
		List<GoodSellInfo> sell_goods_info = new ArrayList<GoodSellInfo>();
		for (int i = 0; i < input.size(); ++i) {
			String temp = input.get(i);
			String[] split_result = temp.split("-");
			
			if(split_result.length < 2){
				num = 1;
			} else {
				num = Integer.parseInt(split_result[1]);
			}
			
			GoodInfo good = good_infos_map.get(split_result[0]);
			if (good != null){
				GoodSellInfo good_selled = new GoodSellInfo(good,num);						
				CalcGoodPrice(good_selled);			
				sell_goods_info.add(good_selled);	
			} else {
				System.out.println("this good doesn't record in system. [barcode:" + split_result[0] +"]");
			}
		}
		return JSONArray.fromObject(sell_goods_info).toString();
	}

	//商品小计,正常售货情况，促销时在子类中重写计算价格函数
	public void CalcGoodPrice(GoodSellInfo good) {
			good.setTotal_price(good.getGood().getPrice()*good.getNum());
			good.setDiscount(0.00);
         	   
	}
	
	public String ParseJsonToReceipt(String json){
		JSONArray json_array = JSONArray.fromObject(json);
		@SuppressWarnings("unchecked")
		List<GoodSellInfo> good_info_list = (List<GoodSellInfo>) 
				JSONArray.toCollection(json_array, GoodSellInfo.class);

		List<GoodSellInfo> discounted_good = new ArrayList<GoodSellInfo>();		
		String return_str = store_name;
		double sum = 0.0;		
		for (int i = 0; i < good_info_list.size(); ++i) {
			GoodSellInfo good = good_info_list.get(i);
			sum += good.getTotal_price();
			return_str += GetGoodReceiptDetail(good);
			if (Math.abs(good.getDiscount() - 0.00) > 0.000001) {
			   discounted_good.add(good);
			}
		}
		
		//如果有折扣商品则添加到小票后面
		if (discounted_good.size() > 0) {
			return_str += "\n" + discount_title + ":";
			double saved_money = 0.0;
			for (int i = 0; i < discounted_good.size(); ++i) {
				GoodSellInfo good_sell_info = discounted_good.get(i);
				saved_money += good_sell_info.getDiscount();
				return_str += "\n名称：" + good_sell_info.getGood().getName()
						+ "\t数量：" + good_sell_info.getNum() + good_sell_info.getGood().getUnit();
			}
			return_str += ("\n\n总计：" + sum + "(" + currency + ")"
					+ " \t节省；" + saved_money + "(" + currency + ")");
		} else {
			return_str += ("\n\n总计：" + sum + "(" + currency + ")");
		}		
		return return_str;
	}
	
	
	private String GetGoodReceiptDetail(GoodSellInfo good){
		String str = "\n名称：" + good.getGood().getName() 
				+ "\t数量：" + good.getNum() 
				+ "\t单价：" + good.getGood().getPrice()
				+ "\t小计：" + good.getTotal_price();
		if (Math.abs(good.getDiscount() - 0.00) > 0.000001) {
			str += ("\t优惠：" + good.getDiscount());
		}
		return str;
	}
	

// getter,setter method
	public Map<String, GoodInfo> getGood_infos_map() {
		return good_infos_map;
	}

	public void setGood_infos_map(Map<String, GoodInfo> good_infos_map) {
		this.good_infos_map = good_infos_map;
	}

	public String getStore_name() {
		return store_name;
	}

	public void setStore_name(String store_name) {
		this.store_name = store_name;
	}
	
	
}
