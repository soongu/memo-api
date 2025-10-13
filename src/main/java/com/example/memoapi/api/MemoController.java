package com.example.memoapi.api;

import com.example.memoapi.dto.MemoDto;
import com.example.memoapi.service.MemoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/memos")
public class MemoController {

    private final MemoService memoService;

    @PostMapping
    public ResponseEntity<MemoDto.Response> createMemo(@RequestBody MemoDto.Request requestDto) {
        MemoDto.Response response = memoService.createMemo(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<MemoDto.Response>> getMemos() {
        List<MemoDto.Response> response = memoService.findAllMemos();
        return ResponseEntity.ok(response);
    }
}