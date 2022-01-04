//
//  TrendingMoviesViewController.swift
//  iosApp
//
//  Created by feanor on 01/01/22.
//  Copyright © 2022 orgName. All rights reserved.
//

import UIKit
import Combine
import Kingfisher

private typealias Datasource = UICollectionViewDiffableDataSource<Section, MovieTileViewModel>
private typealias Snapshot = NSDiffableDataSourceSnapshot<Section, MovieTileViewModel>

enum Section: Hashable {
	case main
}

final class TrendingMoviesViewController: UIViewController, UICollectionViewDelegate {

	private let viewModel: TrendingMoviesViewModel = DI.shared.resolve(TrendingMoviesViewModel.self)

	private var cancellables: Set<AnyCancellable> = Set()

	private var dataSource: Datasource?

	private var selectedMovieId: Int?

	@IBOutlet weak var trendingMoviesCollectionView: UICollectionView!

	override func viewDidLoad() {
        super.viewDidLoad()
		observeViewModel()
		configureCollectionView()

		title = "Trending movies"
    }

	override func viewWillAppear(_ animated: Bool) {
		super.viewWillAppear(animated)
		viewModel.loadAllMovies()
	}

	// MARK: UICollectionViewDelegate

	func collectionView(_ collectionView: UICollectionView, didSelectItemAt indexPath: IndexPath) {
		print("select \(indexPath)")

		guard let itemViewModel = dataSource?.itemIdentifier(for: indexPath) else {
			return
		}

		viewModel.movieClicked(itemViewModel.id)
	}

    // MARK: Private methods

	private func configureCollectionView() {
		configureDatasource()
		trendingMoviesCollectionView.delegate = self
		trendingMoviesCollectionView.collectionViewLayout = generateLayout()
	}

	private func generateLayout() -> UICollectionViewLayout {

		let item = NSCollectionLayoutItem(
			layoutSize: NSCollectionLayoutSize(
				widthDimension: .fractionalWidth(1/2),
				heightDimension: .fractionalHeight(1)))


		let group = NSCollectionLayoutGroup.horizontal(
			layoutSize: NSCollectionLayoutSize(
				widthDimension: .fractionalWidth(1.0),
				heightDimension: .fractionalWidth(3/4)),
			subitems: [item, item])

		let section = NSCollectionLayoutSection(group: group)
		let layout = UICollectionViewCompositionalLayout(section: section)
		return layout

	}

	private func configureDatasource() {
		dataSource = Datasource(collectionView: trendingMoviesCollectionView) { collectionView, indexPath, movieTile in
			let cell = collectionView.dequeueReusableCell(
				withReuseIdentifier: "MovieTileCell",
				for: indexPath) as? MovieTileCell

			cell?.configure(movie: movieTile)

			return cell
		}
	}

	private func observeViewModel() {
		viewModel.bind()

		viewModel.state.sink { [weak self] state in
			self?.handleState(state)
		}.store(in: &cancellables)

		viewModel.actions.sink { [weak self] action in
			self?.handleAction(action)
		}.store(in: &cancellables)
	}

	private func handleState(_ state: TrendingMoviesState) {
		switch state {
		case .loading:
			break
		case .showMovies(let array):
			var snapshot = Snapshot()
			snapshot.appendSections([.main])
			snapshot.appendItems(array)
			dataSource?.apply(snapshot, animatingDifferences: true)
		case .error:
			break
		}
	}

	private func handleAction(_ action: TrendingMoviesAction) {
		switch action {
		case .openMovieDetail(let movieId):
			print("opening movie \(movieId)")
			self.selectedMovieId = movieId
			performSegue(withIdentifier: "detail", sender: self)
		}
	}

	override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
		guard let detailViewController = segue.destination as? MovieDetailViewController, let selectedMovieId = selectedMovieId else {
			return
		}

		detailViewController.movieId = selectedMovieId

	}

}
