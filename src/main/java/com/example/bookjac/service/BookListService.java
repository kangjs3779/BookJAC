package com.example.bookjac.service;

import com.example.bookjac.domain.BookList;
import com.example.bookjac.mapper.BookListMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class BookListService {

    @Autowired
    private BookListMapper mapper;

    public Map<String, Object> bookList(Integer page) {
        // 페이지당 행의 개수
        Integer rowPerPage = 20;

        // 페이지당 데이터 개수 구하기
        Integer startIndex = (page - 1) * rowPerPage;

        // 전체레코드수 구하기
        Integer numOfRecords = mapper.countAll();

        // 마지막페이지 번호 구하기
        Integer lastPageNumber = (numOfRecords - 1) / rowPerPage + 1;

        Map<String, Object> pageInfo = new HashMap<>();
        pageInfo.put("currentPageNum", page);
        pageInfo.put("lastPageNum", lastPageNumber);

        List<BookList> list = mapper.selectAll(startIndex, rowPerPage);
        return Map.of("pageInfo", pageInfo, "bookList", list);
    }
}
