package advisor.objects;

public class PageableObject {
    private static int quantityOfObjectsOnOnePage = 5;
    private static int currentPage;
    private static int quantityOfPages;
    private static int indexOfLastElement;

    public static void setPages(int quantityOfObjects) {
        indexOfLastElement = quantityOfObjects - 1;
        currentPage = 1;
        quantityOfPages = quantityOfObjects / quantityOfObjectsOnOnePage + (quantityOfObjects % quantityOfObjectsOnOnePage > 0 ? 1 : 0);
    }

    public static boolean nextPage() {
        if (currentPage < quantityOfPages) {
            currentPage++;
            return true;
        }
        return false;
    }

    public static boolean prevPage() {
        if (currentPage > 1) {
            currentPage--;
            return true;
        }
        return false;
    }

    public static int getMinIndex() {
        return (currentPage - 1) * quantityOfObjectsOnOnePage;
    }

    public static int getMaxIndex() {
        if (currentPage == quantityOfPages)
            return indexOfLastElement;
        else
            return currentPage * quantityOfObjectsOnOnePage - 1;
    }

    public static int getCurrentPage() {
        return currentPage;
    }

    public static int getQuantityOfPages() {
        return quantityOfPages;
    }

    public static void setQuantityOfObjectsOnOnePage(int quantityOfObjectsOnOnePage) {
        PageableObject.quantityOfObjectsOnOnePage = quantityOfObjectsOnOnePage;
    }
}
