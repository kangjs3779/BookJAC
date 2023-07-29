package com.example.bookjac.mapper;

import com.example.bookjac.domain.Order;
import com.example.bookjac.domain.OrderDetails;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface OrderMapper {
    @Select("""
            SELECT COUNT(*)
            FROM Book
            """)
    Integer countAll();

    @Select("""
            SELECT id,
                   title,
                   writer,
                   publisher,
                   inPrice,
                   totalCount
            FROM Book
            ORDER BY totalCount
            LIMIT #{startIndex}, #{booksInPage}
            """)
    List<Order> selectAllPage(Integer startIndex, Integer booksInPage);

    @Insert("""
            INSERT INTO OrderDetails (name, inserted, totalQuantity, totalPrice)
            VALUES (#{name}, #{inserted}, #{totalQuantity}, #{totalPrice})
            """)
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(OrderDetails od);

    @Select("""
            SELECT COUNT(*)
            FROM OrderDetails
            """)
    Integer countAllOrderDetails();

    @Select("""
            <script>
			<bind name="pattern" value="'%' + search + '%'" />
			SELECT *
			FROM OrderDetails
			WHERE name LIKE #{pattern}
				OR inserted LIKE #{pattern}
			ORDER BY id DESC
			LIMIT #{startIndex}, #{recordsInOrderDetails}
			</script>
            """)
    List<OrderDetails> selectAllPages(Integer startIndex, Integer recordsInOrderDetails, String search);

    /*@Select("""
            SELECT *
            FROM OrderDetails
            WHERE id = #{id}
            """)
    OrderDetails selectById(Integer id);*/
}
