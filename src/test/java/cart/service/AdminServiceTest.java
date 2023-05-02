package cart.service;

import cart.dto.ProductRequestDto;
import cart.entity.ProductEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.assertj.core.groups.Tuple.tuple;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@SpringBootTest
@Transactional
class AdminServiceTest {

    private ProductRequestDto productRequestDto = new ProductRequestDto("케로로", 1000, "https://i.namu.wiki/i/fXDC6tkjS6607gZSXSBdzFq_-12PLPWMcmOddg0dsqRq7Nl30Ek1r23BxxOTiERjGP4eyGmJuVPhxhSpOx2GDw.webp");

    @Autowired
    private AdminService adminService;

    @DisplayName("addProduct 성공 테스트")
    @Test
    void successAddProduct() {
        assertDoesNotThrow(() -> adminService.addProduct(productRequestDto));
    }

    @DisplayName("selectAllProducts 성공 테스트")
    @Test()
    void selectAllProducts() {
        ProductRequestDto productRequestDto2 = new ProductRequestDto("쿠루루", 2000, "https://i.namu.wiki/i/fXDC6tkjS6607gZSXSBdzFq_-12PLPWMcmOddg0dsqRq7Nl30Ek1r23BxxOTiERjGP4eyGmJuVPhxhSpOx2GDw.webp");
        adminService.addProduct(productRequestDto);
        adminService.addProduct(productRequestDto2);

        List<ProductEntity> productEntities = adminService.selectAllProducts();

        assertAll(
                () -> assertThat(productEntities).hasSize(2),
                () -> assertThat(productEntities).extracting("name", "price", "image")
                        .contains(tuple(productRequestDto.getName(), productRequestDto.getPrice(), productRequestDto.getImage())
                                , tuple(productRequestDto2.getName(), productRequestDto2.getPrice(), productRequestDto2.getImage()))
        );
    }

    @DisplayName("updateProduct 성공 테스트")
    @Test
    void updateProduct() {
        int id = adminService.addProduct(productRequestDto);
        ProductRequestDto updateProductDto = new ProductRequestDto("타마마", 100, "https://i.namu.wiki/i/fXDC6tkjS6607gZSXSBdzFq_-12PLPWMcmOddg0dsqRq7Nl30Ek1r23BxxOTiERjGP4eyGmJuVPhxhSpOx2GDw.webp");
        adminService.updateProduct(updateProductDto, id);

        List<ProductEntity> productEntities = adminService.selectAllProducts();

        assertThat(productEntities).extracting("name", "price", "image")
                .contains(tuple(updateProductDto.getName(), updateProductDto.getPrice(), updateProductDto.getImage()));
    }

    @DisplayName("deleteProduct 성공 테스트")
    @Test
    void deleteProduct() {
        int id = adminService.addProduct(productRequestDto);
        adminService.deleteProduct(id);

        List<ProductEntity> productEntities = adminService.selectAllProducts();

        assertThat(productEntities.size()).isZero();
    }
}
