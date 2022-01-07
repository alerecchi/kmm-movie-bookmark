//
//  MovieDetailViewController.swift
//  iosApp
//
//  Created by feanor on 02/01/22.
//  Copyright © 2022 orgName. All rights reserved.
//

import UIKit
import Combine
import Injector
import Kingfisher

private typealias Datasource = UICollectionViewDiffableDataSource<Section, CastMemberViewModel>
private typealias Snapshot = NSDiffableDataSourceSnapshot<Section, CastMemberViewModel>


final class MovieDetailViewController: UIViewController {

	private let viewModel: MovieDetailViewModel = try! Injector.shared.resolve(MovieDetailViewModel.self)

	private var cancellables: Set<AnyCancellable> = Set()

	private var dataSource: Datasource?

	var movieId: Int = 0

	weak var titleLabel: UILabel!
	weak var mainImageView: UIImageView!
	weak var descriptionLabel: UILabel!
	weak var castCollectionView: UICollectionView!

	override func viewDidLoad() {
		super.viewDidLoad()
		observeViewModel()
		buildUI()
		viewModel.loadDetails(movieId)
	}

	override func viewWillDisappear(_ animated: Bool) {
		super.viewWillDisappear(animated)

		viewModel.unbind()
	}

	// MARK: Private methods
	private func observeViewModel() {
		viewModel.bind()

		viewModel
			.state
			.receive(on: RunLoop.main)
			.sink { [weak self] state in
				switch state {

				case .loading:
					break
				case .movieDetail(let detailViewModel):
					self?.configure(detailViewModel: detailViewModel)
				}
			}.store(in: &cancellables)
	}

	private func buildUI() {
		let scrollView = UIScrollView(frame: .zero)
		scrollView.translatesAutoresizingMaskIntoConstraints = false

		scrollView.bounces = false
		let stackView = UIStackView(frame: .zero)
		stackView.translatesAutoresizingMaskIntoConstraints = false
		stackView.axis = .vertical
		stackView.alignment = .fill
		stackView.distribution = .fill
		stackView.spacing = 8
		scrollView.addSubview(stackView)

		stackView.leftAnchor.constraint(equalTo: scrollView.contentLayoutGuide.leftAnchor, constant: 0).isActive = true
		stackView.rightAnchor.constraint(equalTo: scrollView.contentLayoutGuide.rightAnchor, constant: 0).isActive = true
		stackView.topAnchor.constraint(equalTo: scrollView.contentLayoutGuide.topAnchor, constant: 0).isActive = true
		stackView.bottomAnchor.constraint(equalTo: scrollView.contentLayoutGuide.bottomAnchor, constant: 0).isActive = true

		titleLabel = buildTitleLabel(stackView: stackView)
		mainImageView = buildMainImageView(stackView: stackView)
		descriptionLabel = buildDescriptionLabel(stackView: stackView)
		castCollectionView = buildCastCollectionView(stackView: stackView)

		view.addSubview(scrollView)

		scrollView.leftAnchor.constraint(equalTo: view.safeAreaLayoutGuide.leftAnchor, constant: 8).isActive = true
		scrollView.rightAnchor.constraint(equalTo: view.safeAreaLayoutGuide.rightAnchor, constant: -8).isActive = true
		scrollView.topAnchor.constraint(equalTo: view.safeAreaLayoutGuide.topAnchor).isActive = true
		scrollView.bottomAnchor.constraint(equalTo: view.safeAreaLayoutGuide.bottomAnchor).isActive = true
	}

	private func buildTitleLabel(stackView: UIStackView) -> UILabel {
		let label = UILabel(frame: .zero)
		label.translatesAutoresizingMaskIntoConstraints = false
		label.numberOfLines = 0
		label.font = UIFont.preferredFont(forTextStyle: .title1)
		stackView.addArrangedSubview(label)
		return label
	}

	private func buildMainImageView(stackView: UIStackView) -> UIImageView {
		let imageView = UIImageView(frame: .zero)
		imageView.translatesAutoresizingMaskIntoConstraints = false
		stackView.addArrangedSubview(imageView)

		imageView.widthAnchor.constraint(equalToConstant: UIScreen.main.bounds.width-16).isActive = true
		imageView.heightAnchor.constraint(equalTo: imageView.widthAnchor, multiplier: 3/2).isActive = true

		return imageView
	}

	private func buildDescriptionLabel(stackView: UIStackView) -> UILabel {
		let label = UILabel(frame: .zero)
		label.translatesAutoresizingMaskIntoConstraints = false
		label.numberOfLines = 0
		label.font = UIFont.preferredFont(forTextStyle: .caption1)
		stackView.addArrangedSubview(label)
		return label
	}

	private func buildCastCollectionView(stackView: UIStackView) -> UICollectionView {
		let layout = UICollectionViewFlowLayout()
		layout.scrollDirection = .horizontal
		let collectionView = UICollectionView(frame: .zero, collectionViewLayout: layout)
		collectionView.collectionViewLayout = generateLayout()
		collectionView.translatesAutoresizingMaskIntoConstraints = false

		collectionView.heightAnchor.constraint(equalToConstant: 400).isActive = true

		stackView.addArrangedSubview(collectionView)

		collectionView.register(UINib(nibName: "CastMemberCell", bundle: nil), forCellWithReuseIdentifier: "CastMemberCell")

		dataSource = Datasource(collectionView: collectionView, cellProvider: { coll, indexPath, itemViewModel in
			let cell = coll.dequeueReusableCell(withReuseIdentifier: "CastMemberCell", for: indexPath) as? CastMemberCell
			cell?.configure(viewModel: itemViewModel)
			return cell
		})

		return collectionView
	}

	private func configure(detailViewModel: MovieDetailsViewModel) {
		configureTitle(title: detailViewModel.title)
		configureMainImage(imagePath: detailViewModel.posterPath)
		configureOverview(overview: detailViewModel.overview)
		configureCastCollectionView(cast: detailViewModel.cast)
	}

	private func configureCastCollectionView(cast: [CastMemberViewModel]) {
		var snapshot = Snapshot()
		snapshot.appendSections([.main])
		snapshot.appendItems(cast)
		dataSource?.apply(snapshot, animatingDifferences: true)
	}

	private func configureOverview(overview: String?) {
		descriptionLabel.text = overview
	}

	private func configureTitle(title: String) {
		titleLabel.text = title
	}

	private func configureMainImage(imagePath: String?) {
		if let posterPath = imagePath {
			mainImageView.kf.indicatorType = .activity
			mainImageView.kf.setImage(
				with: URL(string: posterPath)!,
				options: [
					.transition(.fade(1)),
					.processor(DownsamplingImageProcessor(size: mainImageView.bounds.size)),
					.scaleFactor(UIScreen.main.scale),
					.cacheOriginalImage
				]
			)
		}
	}

	private func generateLayout() -> UICollectionViewLayout {

		let itemSize = NSCollectionLayoutSize(widthDimension: .fractionalWidth(1),
											  heightDimension: .fractionalHeight(1.0))
		let item = NSCollectionLayoutItem(layoutSize: itemSize)

		let groupSize = NSCollectionLayoutSize(widthDimension: .absolute(200),
											   heightDimension: .absolute(340))
		let group = NSCollectionLayoutGroup.horizontal(layoutSize: groupSize, subitems: [item])


		let section = NSCollectionLayoutSection(group: group)
		section.orthogonalScrollingBehavior = .continuous
		let layout = UICollectionViewCompositionalLayout(section: section)

		return layout

	}
}
