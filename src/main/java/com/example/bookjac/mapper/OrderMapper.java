package com.example.bookjac.mapper;

import com.example.bookjac.domain.Cart;
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
            <script>
			<bind name="pattern" value="'%' + search + '%'" />
            SELECT isbn,
                   title,
                   writer,
                   publisher,
                   inPrice,
                   totalCount
            FROM Book
            WHERE title LIKE #{pattern}
				OR writer LIKE #{pattern}
				OR publisher LIKE #{pattern}
				OR isbn LIKE #{pattern}
            ORDER BY totalCount
            LIMIT #{startIndex}, #{booksInPage}
            </script>
            """)
    List<Order> selectAllPage(Integer startIndex, Integer booksInPage, String search);

    @Insert("""
            INSERT INTO OrderDetails (memberId, name, inserted, totalQuantity, totalPrice)
            VALUES (#{memberId}, #{name}, #{inserted}, #{totalQuantity}, #{totalPrice})
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
			SELECT od.name AS name, od.inserted AS inserted, od.totalQuantity AS totalQuantity, od.totalPrice AS totalPrice, m.id AS memberId
			FROM OrderDetails od
			LEFT JOIN Member m ON od.memberId = m.id
			WHERE od.name LIKE #{pattern}
				OR od.inserted LIKE #{pattern}
			ORDER BY od.id DESC
			LIMIT #{startIndex}, #{recordsInOrderDetails}
			</script>
            """)
    List<OrderDetails> selectAllPages(Integer startIndex, Integer recordsInOrderDetails, String search);

    @Select("""
            SELECT cartId, memberId, bookId, bookCount, title, writer, publisher, inPrice, inserted
            FROM OrderCart
            WHERE inserted = #{inserted} AND memberId = #{memberId}
            """)
    List<Cart> getOrderCart(String inserted, String memberId);

}
