import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class MultiValueLinkList<T> implements List<T> {

	private LinkedListNode<T> first;
	private LinkedListNode<T> last;
	int size = 0;
	private int segmentSize;

	public MultiValueLinkList(int segmentSize) {
		this.first = new LinkedListNode<>(segmentSize);
		this.last = first;
		this.segmentSize = segmentSize;
	}

	@Override
	public int size() {
		return size;
	}

	@Override
	public boolean add(T e) {
		last = last.add(e);
		size++;
		return true;
	}

	@Override
	public void clear() {
		LinkedListNode<T> next = first.nextNode;
		while (next != null) {
			next.clear();
			next = next.nextNode;
		}
		resetNodes();
	}

	private void resetNodes() {
		first.clear();
		first.reInit();
		last.clear();
		last = first;
		size = 0;
	}

	@Override
	public Iterator<T> iterator() {
		return new LinkedListIterator<>();
	}

	@Override
	public boolean isEmpty() {
		return size == 0;
	}

	@Override
	public boolean contains(Object o) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Object[] toArray() {
		throw new UnsupportedOperationException();
	}

	@SuppressWarnings("hiding")
	@Override
	public <T> T[] toArray(T[] a) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean remove(Object o) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean addAll(Collection<? extends T> c) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean addAll(int index, Collection<? extends T> c) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		throw new UnsupportedOperationException();
	}

	@Override
	public T get(int index) {
		throw new UnsupportedOperationException();
	}

	@Override
	public T set(int index, T element) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void add(int index, T element) {
		throw new UnsupportedOperationException();
	}

	@Override
	public T remove(int index) {
		throw new UnsupportedOperationException();
	}

	@Override
	public int indexOf(Object o) {
		throw new UnsupportedOperationException();
	}

	@Override
	public int lastIndexOf(Object o) {
		throw new UnsupportedOperationException();
	}

	@Override
	public ListIterator<T> listIterator() {
		throw new UnsupportedOperationException();
	}

	@Override
	public ListIterator<T> listIterator(int index) {
		throw new UnsupportedOperationException();
	}

	@Override
	public List<T> subList(int fromIndex, int toIndex) {
		throw new UnsupportedOperationException();
	}

	@SuppressWarnings("hiding")
	private class LinkedListIterator<T> implements Iterator<T> {
		private LinkedListNode<T> currentLinkedList;
		int index = -1;

		@SuppressWarnings("unchecked")
		public LinkedListIterator() {
			this.currentLinkedList = (LinkedListNode<T>) MultiValueLinkList.this.first;
		}

		@Override
		public boolean hasNext() {
			index++;
			if (index >= currentLinkedList.values.length) {
				currentLinkedList = currentLinkedList.nextNode;
				if (currentLinkedList == null)
					return false;
				index = -1;
				return hasNext();
			} else {

				if (currentLinkedList.get(index) != null)
					return true;
			}
			return false;
		}

		@Override
		public T next() {
			return currentLinkedList.get(index);
		}

		@Override
		public void remove() {

		}
	}

	public class LinkedListNode<E> {
		private E[] values;
		private LinkedListNode<E> nextNode;
		int curr = 0;

		@SuppressWarnings("unchecked")
		private LinkedListNode(int size) {
			this.values = (E[]) new Object[size];
		}

		private LinkedListNode<E> add(E element) {
			if (curr >= values.length) {
				LinkedListNode<E> tmpNextNode = new LinkedListNode<>(values.length);
				this.nextNode = tmpNextNode;
				return this.nextNode.add(element);
			} else {
				values[curr++] = element;
				return this;
			}
		}

		public E get(int index) {
			return values[index];
		}

		private void clear() {
			if (values == null)
				return;
			for (int index = 0; index < curr; index++) {
				values[index] = null;
			}
			values = null;
		}

		@SuppressWarnings("unchecked")
		public void reInit() {
			this.values = (E[]) new Object[segmentSize];
			curr = 0;
			this.nextNode = null;
		}
	}

}
