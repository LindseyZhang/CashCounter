package logic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;

public class PrintReceiptSellByCategory extends PrintReceipt {
	
	private Map<String, Double> promotion_goods;  //参加按类销售商品的barcode和其参加优惠的阈值
	private double off_percent;
	
	public PrintReceiptSellByCategory(String goods_infos, String promotion_infos,double off_percent){
		super(goods_infos);
		this.off_percent = off_percent;
		promotion_goods = new HashMap<String,Double>();
		LoadPromotionDetail(promotion_infos);
		
	}
	
	public PrintReceiptSellByCategory(
			String goods_infos, 
			String promotion_infos,
			double off_percent,
			int category_num,
			String store_name,
			String discount_title,
			String currency){
		super(goods_infos,store_name,discount_title,currency);
		this.off_percent = off_percent;
		promotion_goods = new HashMap<String,Double>();
		LoadPromotionDetail(promotion_infos);
		
	}
		
	public void LoadPromotionDetail(String json) {
		List<JSONObject> obj_list = new ArrayList<JSONObject>();
		try {
			JSONObject json_obj = JSONObject.fromObject(json);
			obj_list.add(json_obj);
		} catch(JSONException e){
			JSONArray json_array = JSONArray.fromObject(json);	
			for (int i = 0; i < json_array.size(); ++i) {
				JSONObject json_obj = json_array.getJSONObject(i);				
		        obj_list.add(json_obj);
			}
		}
		
		PromotionType type;
		//String type;
		String barcodes;
		for (int i = 0; i < obj_list.size(); ++i) {
			JSONObject json_obj = obj_list.get(i);				
			try{
				//type = (PromotionType) JSONObject.toBean(json_obj, PromotionType.class);
				type = PromotionType.valueOf(json_obj.getString("type"));
				barcodes = json_obj.getString("barcodes");
			} catch (Exception e){
				System.out.println("catch exception:" + e.getMessage());
				continue;
			}
			
			if (barcodes.contains("[") ) {
				JSONArray codes_array = JSONArray.fromObject(barcodes);
				for(int j = 0; j < codes_array.size(); ++j){
					AddPromotionItem(type,codes_array.getString(j));
				}					
			} else {
				AddPromotionItem(type,barcodes);
			}		
		}
					
	}
	
	private void AddPromotionItem(PromotionType type, String barcode) {
		switch (type){
		case SELL_BY_CATEGORY_BY_FIVE: 
			promotion_goods.put(barcode, 5.0);
			break;
		case SELL_BY_CATEGORY_BY_TEN:		
			promotion_goods.put(barcode, 10.0);
			break;
		default:
			System.out.println("wrong promotion type for sell by category." + type);
		}
	}
	
	
	public void CalcGoodPrice(GoodSellInfo good) {
		Double threshold = promotion_goods.get(good.getGood().getBarcode());
		if (threshold == null){
			super.CalcGoodPrice(good);
		} else {
			if (good.getNum() >= threshold) {
				double total_price = good.getGood().getPrice() * good.getNum();
				double after_discount = total_price * off_percent; 
				good.setTotal_price(after_discount);
				good.setDiscount(total_price - after_discount);
			} else {
				good.setTotal_price(good.getGood().getPrice()*good.getNum());
				good.setDiscount(0.00);
			}
		}
	}
	
	//getter, setter method
	public Map<String, Double> getPromotion_goods() {
		return promotion_goods;
	}

	public void setPromotion_goods(Map<String, Double> promotion_goods) {
		this.promotion_goods = promotion_goods;
	}

	public double getOff_percent() {
		return off_percent;
	}

	public void setOff_percent(double off_percent) {
		this.off_percent = off_percent;
	}

}
