package project.code_wiki.service;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import project.code_wiki.common.PageController;
import project.code_wiki.common.ResultMessage;
import project.code_wiki.domain.entity.DocumentEntity;
import project.code_wiki.domain.entity.DocumentDistinctCodeIdEntity;
import project.code_wiki.domain.entity.UserEntity;
import project.code_wiki.domain.entity.BarcodeEntity;
import project.code_wiki.domain.repository.DocumentRepository;
import project.code_wiki.domain.repository.UserRepository;
import project.code_wiki.domain.repository.BarcodeRepository;
import project.code_wiki.dto.*;
import project.code_wiki.exception.NotFoundBarcodeException;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
// 위키 컨트롤러의 요청을 처리하는 객체
@Service
@AllArgsConstructor
public class WikiService {

    private BarcodeRepository barcodeRepository;
    private DocumentRepository documentRepository;
    private UserRepository userRepository;

    // 바코드 정보 추출
    @Transactional
    public BarcodeDto getCode(String id) {
        // 1. 코드 검색
        // Wrapper 객체 선언 (NULL 예외 처리를 위함)
        Optional<BarcodeEntity> barcodeEntityWrapper = barcodeRepository.findById(id);
        // 2. Optional 언박싱
        if(!barcodeEntityWrapper.isPresent()) {
            return null;
        }
        BarcodeEntity barcodeEntity = barcodeEntityWrapper.get();
        // 3. Entity -> DTO 변환 후 리턴
        return convertEntityToBarcodeDto(barcodeEntity);
    }

    // 작성자 정보 추출
    @Transactional
    public UserDto getUserName(String id) {
        // 1. 유저 검색
        // Wrapper 객체 선언 (NULL 예외 처리를 위함)
        Optional<UserEntity> userEntityWrapper = userRepository.findById(id);
        // 2. Optional 언박싱
        if(!userEntityWrapper.isPresent()) {
            return null;
        }
        UserEntity userEntity = userEntityWrapper.get();
        // 3. Entity -> DTO 변환 후 리턴
        return convertEntityToUserDto(userEntity);
    }

    // 위키 문서 추출
    @Transactional
    public WikiDto getDocument(String id, Long revisionDoc) {
        // 1. 해당 리비전 문서 검색
        // Wrapper 객체 선언 (NULL 예외 처리를 위함)
        Optional<DocumentEntity> documentEntityWrapper = documentRepository.findByBarcodeIdAndRevisionDoc(id, revisionDoc);
        // 2. Optional 언박싱
        DocumentEntity documentEntity = documentEntityWrapper.orElse(null);
        // 3. 데이터 모델 변환 후 리턴
        try {
            if (documentEntity==null) {
                throw new NullPointerException();
            }
            // Document Entity -> DTO
            DocumentDto documentDto = convertEntityToDocumentDto(documentEntity);
            // Document DTO -> Wiki DTO
            return convertDocumentToWikiDto(documentDto);
        } catch (NullPointerException e) {
            // 해당 문서가 없으면 exception 발생
            throw new NotFoundBarcodeException(ResultMessage.NOT_EXIST_BARCODE_ID.getValue());
        }
    }

    // 위키 문서 저장
    @Transactional
    public void saveDoc(String id, String writerId, WikiDto wikiDto) {
        // 1. 코드 검색
        BarcodeDto barcodeDto;
        // Wrapper 객체 선언 (NULL 예외 처리를 위함)
        Optional<BarcodeEntity> barcodeEntityWrapper = barcodeRepository.findById(id);
        // 2. Optional 언박싱, 존재하지 않으면 바코드 정보부터 저장
        if(!barcodeEntityWrapper.isPresent()) {
            // 바코드 정보 저장
            barcodeDto = BarcodeDto.builder()
                     .id(id).name(wikiDto.getCodeName()).latelyRevision(0L)
                     .build();
            barcodeRepository.save(barcodeDto.toEntity());
        } else {
            // Barcode Entity -> DTO
            barcodeDto = convertEntityToBarcodeDto(barcodeEntityWrapper.get());
        }
        // 3. 리비전 갱신
        Long revisionDoc = barcodeDto.getLatelyRevision()+1;
        barcodeDto.setLatelyRevision(revisionDoc);
        // 4. 코드 이름이 변경되면 갱신
        if (!barcodeDto.getName().equals(wikiDto.getCodeName())) {
            barcodeDto.setName(wikiDto.getCodeName());
        }
        barcodeRepository.save(barcodeDto.toEntity());
        // 5. 문서 정보 저장
        DocumentDto documentDto = DocumentDto.builder()
                .barcodeId(id).revisionDoc(revisionDoc).userId(writerId).data(wikiDto.getData())
                .build();
        documentRepository.save(documentDto.toEntity());
    }

    // 위키 문서 히스토리 추출
    @Transactional
    public List<HistoryListDto> getHistory(String id, Integer pageNum) {
        // 1. 위키 문서 히스토리 리스트 불러오기(페이지에 출력할 수 있는 개수만큼만 불러오기)
        Page<DocumentEntity> page = documentRepository.findByBarcodeId(id, PageRequest.of(pageNum - 1, PageController.PAGE_POST_COUNT, Sort.by(Sort.Direction.DESC, "id")));
        // 2. Page -> List 변환
        List<DocumentEntity> documentEntities = page.getContent();
        // 3. 데이터 모델 변환 후 리턴
        List<HistoryListDto> historyList = new ArrayList<>();
        for (DocumentEntity documentEntity : documentEntities) {
            // Entity -> DTO
            DocumentDto documentDto = this.convertEntityToDocumentDto(documentEntity);
            // DocumentDTO -> HistoryListDTO
            historyList.add(this.convertDocumentToHistoryDto(documentDto));
        }
        return historyList;
    }

    // 위키 문서 카운트
    @Transactional
    public Long getCodeDocCount(String id) {
        return documentRepository.countByBarcodeId(id);
    }
    // 코드 카운트
    @Transactional
    public Long getCodeCount() {
        return barcodeRepository.count();
    }

    // 위키 리스트 추출
    @Transactional
    public List<OrderedListDto> getWikiList(String orderType, Integer pageNum) {
        // 1. 위키 문서 리스트 불러오기(페이지에 출력할 수 있는 개수만큼만 불러오기)
        Page<DocumentEntity> pageLately=null;
        Page<DocumentDistinctCodeIdEntity> pageLength=null;
        // 정렬 타입에 따른 리스트 불러오기
        switch (orderType) {
            case "lately":
                pageLately=documentRepository.findDocList(this.getPageRequest(pageNum, "DESC"));
                break;
            case "old":
                pageLately=documentRepository.findDocList(this.getPageRequest(pageNum, "ASC"));
            case "longest":
                pageLength=documentRepository.findDocListOrderByLength(this.getPageRequest(pageNum, "DESC"));
                break;
            case "shortest":
                pageLength=documentRepository.findDocListOrderByLengthAsc(this.getPageRequest(pageNum, "DESC"));
                break;
            default:
                break;
        }
        // 2. Page -> Entity
        List<DocumentEntity> documentEntities=null;
        if(pageLately!=null) {
            documentEntities = pageLately.getContent();
        } else if(pageLength!=null) {
            // DocumentDistinctCodeIdEntity List -> DocumentEntity List
            documentEntities = this.convertToDocumentEntity(pageLength.getContent());
        }
        // 3. Entity -> OrderWikiListDTO 변환 후 리턴
        if(documentEntities!=null) {
            return this.convertToWikiList(documentEntities);
        }
        return null;
    }

    // 위키 문서 검색
    @Transactional
    public List<OrderedListDto> searchDoc(String query, Integer pageNum) {
        // 1. 질의에 따른 위키 문서 검색(페이지에 출력할 수 있는 개수만큼만 불러오기)
        Page<BarcodeEntity> page;
        page= barcodeRepository.findByIdContainingOrNameContaining(query, query, this.getPageRequest(pageNum, "DESC"));
        // 2. Page -> List
        List<BarcodeEntity> barcodeEntities = page.getContent();
        // 3. Barcode Entity -> Document Entity 변환
        List<DocumentEntity> documentEntities = this.convertBarcodeToDocumentEntities(barcodeEntities);
        // 4. Document Entity -> OrderedWikiList DTO 변환 후 리턴
        return this.convertToWikiList(documentEntities);
    }
    // 위키 문서 검색
    @Transactional
    public String searchDoc(String query) {
        // 1. 위키 문서 LIKE 검색
        // Wrapper 객체 선언 (NULL 예외 처리를 위함)
        Optional<BarcodeEntity> barcodeEntityWrapper = barcodeRepository.findByIdLikeOrNameLike(query, query);
        // 2. Optional 언박싱 후 결과 존재하면 코드 ID 리턴
        if(barcodeEntityWrapper.isPresent()) {
            BarcodeEntity barcodeEntity = barcodeEntityWrapper.get();
            return barcodeEntity.getId();
        }
        // 3. 없으면 검색 조건 완화 (LIKE -> CONTAINING)
        barcodeEntityWrapper = barcodeRepository.findByIdContainingOrNameContaining(query, query);
        // 4. Optional 언박싱 후 결과 존재하면 코드 ID 리턴
        if (barcodeEntityWrapper.isPresent()) {
            BarcodeEntity barcodeEntity = barcodeEntityWrapper.get();
            return barcodeEntity.getId();
        }
        // 5. 그래도 없으면 null 리턴
        return null;
    }

    // 무작위 위키 문서 추출
    @Transactional
    public String shuffleDoc() {
        // 1. 위키 문서 LIKE 검색
        // Wrapper 객체 선언 (NULL 예외 처리를 위함)
        Optional<BarcodeEntity> barcodeEntityWrapper = barcodeRepository.findIdRandom();
        // 2. Optional 언박싱 후 결과 존재하면 코드 ID 리턴
        if(barcodeEntityWrapper.isPresent()) {
            BarcodeEntity barcodeEntity = barcodeEntityWrapper.get();
            return barcodeEntity.getId();
        }
        // 3. 없으면 null 리턴
        return null;
    }

    // 페이지 요구사항 반환 객체(정렬상태에 따른 반환)
    private PageRequest getPageRequest(Integer pageNum, String orderType) {
        PageRequest pageRequest = null;
        if (orderType.equals("DESC")) {
            pageRequest=PageRequest.of(pageNum-1, PageController.PAGE_POST_COUNT, Sort.by((Sort.Direction.DESC), "id"));
        } else if (orderType.equals("ASC")) {
            pageRequest=PageRequest.of(pageNum-1, PageController.PAGE_POST_COUNT, Sort.by((Sort.Direction.ASC), "id"));
        }
        return pageRequest;
    }

    // Barcode Entity -> Document Entity 변환
    private List<DocumentEntity> convertBarcodeToDocumentEntities(List<BarcodeEntity> entities) {
        List<DocumentEntity> list = new ArrayList<>();
        for(BarcodeEntity entity : entities) {
            Optional<DocumentEntity> entityWrapper = documentRepository.findByBarcodeIdAndRevisionDoc(entity.getId(), entity.getLatelyRevision());
            DocumentEntity documentEntity = entityWrapper.orElse(null);
            list.add(documentEntity);
        }
        return list;
    }
    // DocumentDistinctCodeIdEntity List -> DocumentEntity List 변환
    private List<DocumentEntity> convertToDocumentEntity(List<DocumentDistinctCodeIdEntity> entities) {
        List<DocumentEntity> list = new ArrayList<>();
        for(DocumentDistinctCodeIdEntity entity : entities) {
            Optional<DocumentEntity> entityWrapper = documentRepository.findByBarcodeIdAndRevisionDoc(entity.getBarcode_id(), entity.getRevision_doc());
            DocumentEntity documentEntity = entityWrapper.orElse(null);
            list.add(documentEntity);
        }
        return list;
    }
    // Document Entity -> OrderWikiList DTO 변환
    private List<OrderedListDto> convertToWikiList(List<DocumentEntity> documentEntities) {
        List<OrderedListDto> wikiList = new ArrayList<>();
        for(DocumentEntity documentEntity : documentEntities) {
            BarcodeDto barcodeDto = this.getCode(documentEntity.getBarcodeId());
            DocumentDto documentDto = this.convertEntityToDocumentDto(documentEntity, barcodeDto);
            wikiList.add(this.convertDocumentToOrderedListDto(documentDto));
        }
        return wikiList;
    }
    // Barcode Entity -> DTO 변환
    private BarcodeDto convertEntityToBarcodeDto(BarcodeEntity barcodeEntity) {
        return BarcodeDto.builder()
                .id(barcodeEntity.getId())
                .name(barcodeEntity.getName())
                .latelyRevision(barcodeEntity.getLatelyRevision())
                .build();
    }
    // User Entity -> DTO 변환
    private UserDto convertEntityToUserDto(UserEntity userEntity) {
        return UserDto.builder()
                .email(userEntity.getEmail()).name(userEntity.getName()).password(null)
                .build();
    }
    // Document Entity -> DTO 변환 (최신 리비전)
    private DocumentDto convertEntityToDocumentDto(DocumentEntity documentEntity, BarcodeDto barcodeDto) {
        return DocumentDto.builder()
                .id(documentEntity.getId())
                .barcodeId(documentEntity.getBarcodeId())
                .revisionDoc(barcodeDto.getLatelyRevision())
                .userId(documentEntity.getUserId())
                .data(documentEntity.getData())
                .updateDate(documentEntity.getUpdateDate())
                .build();
    }
    // Document Entity -> DTO 변환 (히스토리 리비전)
    private DocumentDto convertEntityToDocumentDto(DocumentEntity documentEntity) {
        return DocumentDto.builder()
                .id(documentEntity.getId())
                .barcodeId(documentEntity.getBarcodeId())
                .revisionDoc(documentEntity.getRevisionDoc())
                .userId(documentEntity.getUserId())
                .data(documentEntity.getData())
                .updateDate(documentEntity.getUpdateDate())
                .build();
    }
    // Document DTO -> Wiki DTO 변환
    private WikiDto convertDocumentToWikiDto(DocumentDto documentDto) {
        BarcodeDto barcodeDto = this.getCode(documentDto.getBarcodeId());
        return WikiDto.builder()
                .codeId(documentDto.getBarcodeId())
                .codeName(barcodeDto.getName())
                .updateDate(documentDto.getUpdateDate())
                .data(documentDto.getData())
                .build();
    }
    // DocumentDTO -> HistoryListDTO 변환
    private HistoryListDto convertDocumentToHistoryDto(DocumentDto documentDto) {
        BarcodeDto barcodeDto = this.getCode(documentDto.getBarcodeId());
        UserDto userDto = this.getUserName(documentDto.getUserId());
        return HistoryListDto.builder()
                .codeId(barcodeDto.getId())
                .codeName(barcodeDto.getName())
                .userName(userDto.getName())
                .revisionDoc(documentDto.getRevisionDoc())
                .updateDate(documentDto.getUpdateDate())
                .build();
    }
    // DocumentDto -> OrderedListDto 변환
    private OrderedListDto convertDocumentToOrderedListDto(DocumentDto documentDto) {
        BarcodeDto barcodeDto = this.getCode(documentDto.getBarcodeId());
        UserDto userDto = this.getUserName(documentDto.getUserId());
        return OrderedListDto.builder()
                .codeId(barcodeDto.getId())
                .codeName(barcodeDto.getName())
                .userName(userDto.getName())
                .updateDate(documentDto.getUpdateDate())
                .build();
    }

    // 관리자: 바코드 데이터 전부 불러오기
    @Transactional
    public List<BarcodeDto> getBarcodeData() {
        List<BarcodeEntity> barcodeEntities = barcodeRepository.findAll();
        List<BarcodeDto> barcodeDtoList = new ArrayList<>();
        for(BarcodeEntity barcodeEntity : barcodeEntities) {
            barcodeDtoList.add(this.convertEntityToBarcodeDto(barcodeEntity));
        }

        return barcodeDtoList;
    }

    // 바코드 데이터 삭제
    @Transactional
    public void deleteBarcode(String id) {
        barcodeRepository.deleteById(id);
    }

    @Transactional
    public List<DocumentDto> getDocumentData() {
        List<DocumentEntity> documentEntities = documentRepository.findAll();
        List<DocumentDto> documentDtoList = new ArrayList<>();
        for(DocumentEntity documentEntity : documentEntities) {
            documentDtoList.add(this.convertEntityToDocumentDto(documentEntity));
        }

        return documentDtoList;
    }

    @Transactional
    public void deleteDocument(Long id) {
        documentRepository.deleteById(id);
    }

    public List<DataStatisticDto> getCodeWeekCount() {
        return documentRepository.getCodeWeekCount();
    }

    @Transactional
    public List<DataStatisticDto> getRevisionWeekCount() {
        return documentRepository.getRevisionWeekCount();
    }
}