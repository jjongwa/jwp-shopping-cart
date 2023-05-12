package cart.service;

import cart.dao.ProductDao;
import cart.dto.ProductRequestDto;
import cart.entity.ProductEntity;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AdminService {
    private final ProductDao productDao;

    public AdminService(ProductDao productDao) {
        this.productDao = productDao;
    }


    public int addProduct(ProductRequestDto productRequestDto) {
        ProductEntity productEntity = new ProductEntity(productRequestDto.getName(), productRequestDto.getPrice(),
                productRequestDto.getImage());
        return productDao.insertProduct(productEntity);
    }

    @Transactional(readOnly = true)
    public List<ProductEntity> selectAllProducts() {
        return productDao.selectAllProducts();
    }

    public void updateProduct(ProductRequestDto productRequestDto, int productId) {
        if (!productDao.isProductExist(productId)) {
            throw new IllegalArgumentException("수정하려는 제품이 존재하지 않습니다.");
        }

        ProductEntity productEntity = new ProductEntity(productId, productRequestDto.getName(),
                productRequestDto.getPrice(), productRequestDto.getImage());
        productDao.updateProduct(productEntity);
    }

    public void deleteProduct(int productId) {
        if (!productDao.isProductExist(productId)) {
            throw new IllegalArgumentException("삭제하려는 제품이 존재하지 않습니다.");
        }

        productDao.deleteProduct(productId);
    }

    public void deleteAll() {
        productDao.deleteAllProduct();
    }

}
