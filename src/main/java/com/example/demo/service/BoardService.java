package com.example.demo.service;


import com.example.demo.domain.Board;
import com.example.demo.domain.User;
import com.example.demo.dto.BoardListResponseDto;
import com.example.demo.dto.BoardRequestDto;
import com.example.demo.dto.BoardResponseDto;
import com.example.demo.jwt.JwtUtil;
import com.example.demo.repository.BoardRepository;
import com.example.demo.repository.MemberRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class BoardService {

    private final MemberRepository memberRepository;
    private final BoardRepository boardRepository;
    // 글 생성
//    public BoardResponseDto createBoard(User user, BoardRequestDto requestDto) {
//        Board board = new Board(user, requestDto);
//        user = memberRepository.findById(JwtUtil.getCurrentMemberId()).orElseThrow(()-> new RuntimeException("로그인 유저 정보가 없습니다."));
//        boardRepository.save(board);
//        return new BoardResponseDto(user, board);
//    }

    public BoardResponseDto createBoard(User user, BoardRequestDto requestDto) {
        // user 객체는 이미 파라미터로 전달받았으므로 다시 가져올 필요가 없습니다.

        // board 객체를 생성합니다.
        Board board = new Board(user, requestDto);

        // board 객체를 저장합니다.
        boardRepository.save(board);

        // BoardResponseDto를 생성하여 반환합니다.
        return new BoardResponseDto(user, board);
    }

    // 모든 글 가져오기
    public List<BoardListResponseDto> findAllBoard() {
        return boardRepository.findAllByOrderByModifiedAtDesc();
    }

    // 글 하나 가져오기
    public BoardResponseDto findOneBoard(Long id) {
        Board board = boardRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("게시글이 없습니다")
        );
        return new BoardResponseDto(board);
    }

    // 글 수정
    @Transactional
    public Long updateBoard(User user, Long id, BoardRequestDto requestDto) {
        Board board = boardRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("해당 아이디가 존재하지 않습니다.")
        );
        if (user.getName().equals(board.getUser().getName())) {
            board.update(requestDto);
            return board.getId();
        } else {
            throw new IllegalArgumentException("작성자만 수정할 수 있습니다");
        }
    }

    // 삭제
    @Transactional
    public Long deleteBoard(User user, Long id) {
        // 어떤 게시판인지 찾기
        Board board = boardRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다.")
        );

        // 게시판의 username과 로그인 유저의 username 비교
        if (user.getName().equals(board.getUser().getName())) {
            boardRepository.deleteById(id);
            return id;
        } else {
            throw new IllegalArgumentException("작성자만 수정할 수 있습니다");
        }
    }
}