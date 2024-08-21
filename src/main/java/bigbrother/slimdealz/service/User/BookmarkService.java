package bigbrother.slimdealz.service.User;

import bigbrother.slimdealz.entity.Bookmark;
import bigbrother.slimdealz.entity.product.Product;
import bigbrother.slimdealz.repository.User.BookmarkRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookmarkService {

    private final BookmarkRepository bookmarkRepository;

    public List<Product> getUserBookmarks(Long memberId) {
        List<Bookmark> bookmarks = bookmarkRepository.findByMemberId(memberId);
        return bookmarks.stream()
                .map(Bookmark::getProduct)
                .collect(Collectors.toList());
    }
}
