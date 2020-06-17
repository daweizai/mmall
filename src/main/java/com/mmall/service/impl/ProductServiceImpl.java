package com.mmall.service.impl;

import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.dao.ProductDao;
import com.mmall.pojo.Product;
import com.mmall.service.IProductService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 商品业务层
 *
 * @Author daweizai
 * @Date 22:28 2020/6/17
 * @ClassName ProductServiceImpl
 * @Version 1.0
 **/
@Service("iProductService")
public class ProductServiceImpl implements IProductService {

    @Autowired
    private ProductDao productDao;

    @Override
    public ServerResponse<String> saveOrUpdateProduct(Product product) {
        if (product != null) {
            //设置图片
            if (StringUtils.isNotBlank(product.getSubImages())) {
                String[] subImageArray = product.getSubImages().split(",");
                if (subImageArray.length > 0) {
                    product.setMainImage(subImageArray[0]);
                }
            }
            if (product.getId() != null) {
                int rowCount = productDao.updateByPrimaryKey(product);
                if (rowCount > 0) {
                    return ServerResponse.successByMsg("更新商品成功");
                }
                return ServerResponse.error("更新商品失败");
            } else {
                int rowCount = productDao.insert(product);
                if (rowCount > 0) {
                    return ServerResponse.successByMsg("新增产品成功");
                }
                return ServerResponse.error("新增产品失败");
            }
        }
        return ServerResponse.error("新添或更新产品参数不正确。");
    }

    public ServerResponse<String> setSaleStatus(Integer productId, Integer status) {
        if (productId == null || status == null) {
            return ServerResponse.error(ResponseCode.ILLEGAL_ARGUMENT);
        }
        Product product = new Product();
        product.setId(productId);
        product.setStatus(status);
        int rowCount = productDao.updateByPrimaryKeySelective(product);
        if (rowCount > 0) {
            return ServerResponse.successByMsg("修改商品销售状态成功");
        }
        return ServerResponse.error("修改商品销售状态失败");

    }
}
