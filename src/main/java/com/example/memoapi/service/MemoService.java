package com.example.memoapi.service;

import com.example.memoapi.dto.MemoDto;
import com.example.memoapi.entity.Memo;
import com.example.memoapi.repository.MemoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemoService {

    private final MemoRepository memoRepository;

    @Transactional
    public MemoDto.Response createMemo(MemoDto.Request requestDto) {
        Memo savedMemo = memoRepository.save(requestDto.toEntity());
        return MemoDto.Response.from(savedMemo);
    }

    public List<MemoDto.Response> findAllMemos() {
        return memoRepository.findAll().stream()
                .map(MemoDto.Response::from)
                .collect(Collectors.toList());
    }
}